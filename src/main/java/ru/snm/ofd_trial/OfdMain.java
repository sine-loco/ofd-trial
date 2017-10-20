package ru.snm.ofd_trial;

import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import ru.snm.ofd_trial.customer.OfdCustomerFacade;
import ru.snm.ofd_trial.http.OfdCustomerHandler;
import ru.snm.ofd_trial.xml.SimpleXmlFunctions;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Properties;

/**
 * @author snm
 */
public class OfdMain {
    private final static Logger logger = LogManager.getLogger();

    public static void main( String[] args ) {
        // fixme read external ones
        Properties props = new Properties();
        props.setProperty( OfdConfig.PROP_PATH, "/ofd" );
        props.setProperty( OfdConfig.PROP_PORT, "8080" );


        OfdConfig config = OfdConfig.getConfig( props );

        OfdGlobalContext.initContext(
                SimpleXmlFunctions::deserialize,
                SimpleXmlFunctions::serialize,
                OfdCustomerFacade::processRequest );

        HttpServer server = createAndStartHttp( config );

        logger.info( "server started" );

        Runtime.getRuntime().addShutdownHook( new Thread( () -> {
            logger.trace( "will stop the server" );
            server.stop( 0 );
        } ) );
    }

    public static HttpServer createAndStartHttp( OfdConfig config ) {
        final String path = config.path;
        final int port = config.port;

        URI baseUri = UriBuilder.fromUri( "http://localhost" ).port( port ).path( path )
                .build();
        ResourceConfig rc = new ResourceConfig( OfdCustomerHandler.class );
        return JdkHttpServerFactory.createHttpServer( baseUri, rc );
    }
}
