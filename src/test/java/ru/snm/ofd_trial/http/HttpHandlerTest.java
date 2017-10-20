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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.powermock.api.mockito.PowerMockito;
import ru.snm.ofd_trial.OfdConfig;
import ru.snm.ofd_trial.OfdGlobalContext;
import ru.snm.ofd_trial.OfdMain;
import ru.snm.ofd_trial.customer.OfdCustomerAction;
import ru.snm.ofd_trial.customer.OfdCustomerFacade;
import ru.snm.ofd_trial.xml.OfdDeserializer;
import ru.snm.ofd_trial.xml.OfdResponse;
import ru.snm.ofd_trial.xml.OfdSerializer;
import ru.snm.ofd_trial.xml.SimpleXmlFunctions;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;
import static ru.snm.ofd_trial.customer.OfdCustomerFacade.TECH_ERROR_RESPONSE;

/**
 * Tests that only POST with XML payload is accepted.
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
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
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
    static void setUpTestContext() {
        // all "beans" are just mocks - no real work is needed
        OfdGlobalContext.initContext(
                PowerMockito.mock( OfdDeserializer.class ),
                // spoils default response, but this test does not check that behavior
                PowerMockito.mock( OfdSerializer.class, invocationOnMock -> "<fake/>" ),
                PowerMockito.mock( OfdCustomerAction.class ) );

        Properties props = new Properties();
        props.setProperty( OfdConfig.PROP_PATH, "/ofd" );
        // there ought to be random port, but...
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


    @Tag( "integration" )
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