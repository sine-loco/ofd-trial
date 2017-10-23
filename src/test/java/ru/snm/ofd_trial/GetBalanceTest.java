package ru.snm.ofd_trial;

import com.jolbox.bonecp.BoneCPDataSource;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.snm.ofd_trial.domain.common.OfdCustomerFacade;
import ru.snm.ofd_trial.domain.common.OfdDtoHelper;
import ru.snm.ofd_trial.domain.common.OfdResponse;
import ru.snm.ofd_trial.xml.SimpleXmlFunctions;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.powermock.api.mockito.PowerMockito.mock;
import static ru.snm.ofd_trial.OfdMain.datasource;
import static ru.snm.ofd_trial.OfdMain.flyway;
import static ru.snm.ofd_trial.OfdMain.startHttpServer;
import static ru.snm.ofd_trial.xml.SimpleXmlFunctions.deserialize;

/**
 * @author snm
 */
public class GetBalanceTest {
    private final static Logger logger = LogManager.getLogger();

    private static HttpServer server;
    private static OfdConfig config;
    private static String URI;

    final static String login = "l";
    final static String password = "p";
    final static String balance = "99.99";

    private static HttpPost makeXmlPost( String rq ) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost( URI );
        post.setEntity( new StringEntity( rq ) );
        post.setHeader( "Accept", "application/xml" );
        post.setHeader( "Content-type", "application/xml" );
        return post;
    }

    @BeforeAll
    static void setUpTestContext() throws SQLException {
        Properties props = new Properties();
        props.setProperty( OfdConfig.PROP_PATH, "/ofd" );
        // there ought to be random port, but...
        props.setProperty( OfdConfig.PROP_PORT, "8890" );
        props.setProperty( OfdConfig.PROP_DB_URL, "jdbc:hsqldb:mem:gbDb" );

        config = new OfdConfig( props );

        BoneCPDataSource datasource = datasource( config );
        Flyway flyway = flyway( datasource );

        OfdGlobalContext.initContext(
                SimpleXmlFunctions::deserialize,
                SimpleXmlFunctions::serialize,
                OfdCustomerFacade::processRequest,
                datasource,
                flyway,
                config );

        flyway.migrate();
        insertRow();

        server = startHttpServer( config );
        URI = "http://localhost:" + config.port + config.path;
    }

    @AfterAll
    static void tearDown() {
        OfdGlobalContext.clearContext();
        server.stop( 0 );
    }

    static void insertRow() throws SQLException {
        try (
                Connection conn =
                        OfdGlobalContext.getContext().getDataSource().getConnection()
        ) {
            Statement s = conn.createStatement();
            s.executeUpdate(
                    "insert into ofd.account ( login, password, balance ) " +
                    "values ( '" + login + "', '" + password + "', " + balance + " );" );
        }
    }

    @Tag( "integration" )
    @ParameterizedTest
    @CsvFileSource( resources = "/xml/integration--get-balance.csv",
            lineSeparator = "??", delimiter = '~' )
    @DisplayName( "get balance integration test" )
    void getBalance( String rq, int expectedCode ) throws UnsupportedEncodingException {
        logger.info( "input: \n{}", rq );

        rq = rq.replace( "${user}", login ).replace( "${password}", password );

        HttpPost post = makeXmlPost( rq );

        try (
                CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = client.execute( post ) )
        {
            String xmlRs;
            HttpEntity entity = response.getEntity();
            try ( InputStream is = entity.getContent() ) {
                 xmlRs = new Scanner( is, UTF_8.name() ).useDelimiter( "\\Z" ).next();
            }
            logger.info( "output: \n{}", xmlRs );
            OfdResponse rs = deserialize( xmlRs, OfdResponse.class );
            assertAll(
                    () -> assertEquals( expectedCode, rs.resultCode ),
                    () -> {
                        if ( expectedCode == 0 ) {
                            assertEquals( balance, OfdDtoHelper.getBalanceFrom( rs ) );
                        }
                    }
            );
        } catch ( Exception e ) {
            logger.error( e.getMessage(), e );
            fail( "should not throw" );
        }
    }
}
