package ru.snm.ofd_trial.http;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.snm.ofd_trial.OfdConfig;
import ru.snm.ofd_trial.OfdMain;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author snm
 */
class HttpHandlerTest {
    private final static Logger logger = LogManager.getLogger();

    private static HttpServer server;

    private static OfdConfig config;


    private static HttpEntityEnclosingRequestBase addXmlInto(
            HttpEntityEnclosingRequestBase http )
            throws UnsupportedEncodingException
    {
        String xml =
                "<?xml version=\"1.0\" encoding=\"utf-8\"" +
                        "<tag></tag>";
        http.setEntity( new StringEntity( xml ) );
        http.setHeader( "Accept", "application/xml" );
        http.setHeader( "Content-type", "application/xml" );

        return http;
    }


    private static HttpEntityEnclosingRequestBase addJsonInto(
            HttpEntityEnclosingRequestBase http )
            throws UnsupportedEncodingException
    {
        String json = "{ \"name\":\"value\"";
        http.setEntity( new StringEntity( json ) );
        http.setHeader( "Accept", "application/json" );
        http.setHeader( "Content-type", "application/json" );

        return http;
    }

    @BeforeAll
    static void setUp() {
        Properties props = new Properties();
        props.setProperty( OfdConfig.PROP_PATH, "/ofd" );
        // there ought to be Spring's random port analog, but... no need of it
        props.setProperty( OfdConfig.PROP_PORT, "8888" );
        config = OfdConfig.getConfig( props );
        server = OfdMain.createAndStartHttp( config );
    }

    @AfterAll
    static void tearDown() { server.stop( 0 ); }

    static Stream<Arguments> requestAndCode() throws UnsupportedEncodingException {
        final String uri = "http://localhost:" + config.port + config.path;

        return Stream.of(
                Arguments.of( new HttpGet( uri ), 405 ),
                Arguments.of( new HttpDelete( uri ), 405 ),
                Arguments.of( new HttpHead( uri ), 405 ),
                Arguments.of( addXmlInto( new HttpPut( uri ) ), 405 ),
                Arguments.of( addXmlInto( new HttpPost( uri ) ), 200 ),
                Arguments.of( addJsonInto( new HttpPost( uri ) ), 415 )
                // the rest of HTTP methods matter not
        );
    }


    @ParameterizedTest
    @MethodSource( "requestAndCode" )
    @DisplayName( "test that only POST with XML is accepted" )
    void handleRequest( HttpUriRequest rq, int expectedCode ) {
        try (
                CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse response = client.execute( rq ) )
        {
            HttpEntity entity = response.getEntity();
            EntityUtils.consume( entity );

            int actualCode = response.getStatusLine().getStatusCode();
            assertEquals( expectedCode, actualCode );
        } catch ( Exception e ) {
            logger.error( e );
            fail( "should not throw" );
        }
    }
}