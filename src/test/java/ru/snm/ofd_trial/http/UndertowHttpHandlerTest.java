package ru.snm.ofd_trial.http;

import io.undertow.Undertow;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.snm.ofd_trial.OfdConfig;

import javax.servlet.ServletException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author snm
 */
class UndertowHttpHandlerTest {
    private final static Logger logger = LogManager.getLogger();

    private Undertow server;

    @BeforeAll
    void setUp() throws ServletException {
        server = UndertowServer.createUndertow( OfdConfig.getConfig() );
        server.start();
    }

    @AfterAll
    void tearDown() { server.stop(); }

    @ParameterizedTest
    @MethodSource( "requestAndCode" )
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

    static Stream<Arguments> requestAndCode() {
        final String uri = "http://localhost:8080/ofd";
        return Stream.of(
                Arguments.of( new HttpGet( uri ), 405 ),
                Arguments.of( new HttpPost( uri ), 200 ),
                Arguments.of( new HttpPut( uri ), 405 ),
                Arguments.of( new HttpDelete( uri ), 405 ),
                Arguments.of( new HttpHead( uri ), 405 )
                // the rest of HTTP methods matter not
        );
    }
}