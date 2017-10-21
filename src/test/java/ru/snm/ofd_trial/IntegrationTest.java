package ru.snm.ofd_trial;

import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.powermock.api.mockito.PowerMockito;
import ru.snm.ofd_trial.customer_service.OfdCustomerAction;
import ru.snm.ofd_trial.customer_service.OfdCustomerFacade;
import ru.snm.ofd_trial.xml.OfdDeserializer;
import ru.snm.ofd_trial.xml.OfdSerializer;
import ru.snm.ofd_trial.xml.SimpleXmlFunctions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author snm
 */
class IntegrationTest {
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
        // no substitutions
        OfdGlobalContext.initContext(
                SimpleXmlFunctions::deserialize,
                SimpleXmlFunctions::serialize,
                OfdCustomerFacade::processRequest );

        Properties props = new Properties();
        props.setProperty( OfdConfig.PROP_PATH, "/ofd" );
        // there ought to be random port, but...
        props.setProperty( OfdConfig.PROP_PORT, "8880" );
        config = OfdConfig.getConfig( props );

        server = OfdMain.createAndStartHttp( config );
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