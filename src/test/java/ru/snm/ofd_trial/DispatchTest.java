package ru.snm.ofd_trial;

import com.jolbox.bonecp.BoneCPDataSource;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.snm.ofd_trial.domain.common.OfdCustomerFacade;
import ru.snm.ofd_trial.xml.SimpleXmlFunctions;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * @author snm
 */
class DispatchTest {
    private final static Logger logger = LogManager.getLogger();

    private static HttpServer server;
    private static OfdConfig config;
    private static String URI;

    private static HttpPost makeXmlPost( String rq ) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost( URI );
        post.setEntity( new StringEntity( rq ) );
        post.setHeader( "Accept", "application/xml" );
        post.setHeader( "Content-type", "application/xml" );
        return post;
    }

    @BeforeAll
    static void setUpTestContext() {
        Properties props = new Properties();
        props.setProperty( OfdConfig.PROP_PATH, "/ofd" );
        // there ought to be random port, but...
        props.setProperty( OfdConfig.PROP_PORT, "8880" );
        config = new OfdConfig( props );

        OfdGlobalContext.initContext(
                SimpleXmlFunctions::deserialize,
                SimpleXmlFunctions::serialize,
                OfdCustomerFacade::processRequest,
                mock( BoneCPDataSource.class ),
                mock( Flyway.class ),
                config );


        server = OfdMain.startHttpServer( config );
        URI = "http://localhost:" + config.port + config.path;
    }

    @AfterAll
    static void tearDown() {
        OfdGlobalContext.clearContext();
        server.stop( 0 );
    }

    @Tag( "integration" )
    @ParameterizedTest
    @CsvFileSource( resources = "/xml/integration.csv", lineSeparator = "??" )
    @DisplayName( "integration test" )
    void integration( String rq ) throws UnsupportedEncodingException {
        logger.info( "input: \n{}", rq );

        HttpPost post = makeXmlPost( rq );

        try (
                CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = client.execute( post ) )
        {
            HttpEntity entity = response.getEntity();
            try ( InputStream is = entity.getContent() ) {
                String actualRs =
                        new Scanner( is, UTF_8.name() ).useDelimiter( "\\Z" ).next();

                logger.info( "output: \n{}", actualRs );
            }

        } catch ( Exception e ) {
            logger.error( e.getMessage(), e );
            fail( "should not throw" );
        }
    }


}