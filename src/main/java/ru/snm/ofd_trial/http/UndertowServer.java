package ru.snm.ofd_trial.http;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.snm.ofd_trial.OfdConfig;

import javax.servlet.ServletException;

/**
 * @author snm
 */
public class UndertowServer {
    private final static Logger logger = LogManager.getLogger();

    public static void main( String[] args ) throws Exception {
        // fixme read properties
        OfdConfig config = OfdConfig.getConfig();

        Undertow server = createUndertow( config );
        server.start();

        logger.info( "server started" );

        Runtime.getRuntime().addShutdownHook( new Thread( () -> {
            logger.trace( "will stop the server" );
            server.stop();
        } ) );
    }

    public static Undertow createUndertow( OfdConfig config ) throws ServletException {
        final String path = config.getPath();
        final int port = config.getPort();

        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader( UndertowServer.class.getClassLoader() )
                .setContextPath( path )
                .setDeploymentName( "ofd-trial.war" )
                .addServlets( Servlets
                        .servlet( "ofd", OfdServlet.class ).addMapping( "/*" ) );

        DeploymentManager manager =
                Servlets.defaultContainer().addDeployment( servletBuilder );
        manager.deploy();
        PathHandler ph = Handlers.path( Handlers.redirect( path ) )
                .addPrefixPath( path, manager.start() );

        return Undertow.builder()
                .addHttpListener( port, "localhost" )
                .setHandler( ph )
                .build();
    }
}
