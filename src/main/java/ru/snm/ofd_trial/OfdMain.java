package ru.snm.ofd_trial;

import com.jolbox.bonecp.BoneCPDataSource;
import com.sun.net.httpserver.HttpServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import ru.snm.ofd_trial.domain.common.OfdCustomerFacade;
import ru.snm.ofd_trial.http.OfdCustomerHandler;
import ru.snm.ofd_trial.xml.SimpleXmlFunctions;

import javax.sql.DataSource;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Properties;

/**
 * @author snm
 */
public class OfdMain {
    private final static Logger logger = LogManager.getLogger();

    public static void main( String[] args ) {
        try {
            Properties props = new Properties();
            props.load( OfdMain.class.getClassLoader()
                    .getResourceAsStream( "ofd-trial.properties" ) );

            OfdConfig config = new OfdConfig( props );

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

            HttpServer httpServer = startHttpServer( config );

            logger.info( "HTTP server started" );

            Runtime.getRuntime().addShutdownHook( new Thread( () -> {
                logger.trace( "will clear global context" );
                OfdGlobalContext.clearContext();

                logger.trace( "will stop the HTTP server" );
                httpServer.stop( 0 );
            } ) );
        } catch ( Exception e ) {
            logger.error( "could not start", e );
        }
    }

    public static Flyway flyway( DataSource datasource ) {
        Flyway flyway = new Flyway();
        flyway.setDataSource( datasource );
        flyway.setSchemas( "OFD" );

        logger.debug( "flyway configured" );

        return flyway;
    }

    public static BoneCPDataSource datasource( OfdConfig config ) {
        // Class.forName( config.dbDriver);
        BoneCPDataSource ds = new BoneCPDataSource();
        ds.setJdbcUrl( config.dbUrl );
        ds.setUsername( config.dbUser );
        ds.setPassword( config.dbPassword );
        ds.setDefaultAutoCommit( true );
        
        logger.debug( "datasource configured" );

        return ds;
    }

    public static HttpServer startHttpServer( OfdConfig config ) {
        final String path = config.path;
        final int port = config.port;

        URI baseUri = UriBuilder.fromUri( "http://localhost" ).port( port ).path( path )
                .build();
        ResourceConfig rc = new ResourceConfig( OfdCustomerHandler.class );
        return JdkHttpServerFactory.createHttpServer( baseUri, rc );
    }
}
