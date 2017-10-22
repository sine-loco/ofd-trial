package ru.snm.ofd_trial;

import org.hsqldb.server.Server;


/**
 * @author snm
 */
public class HsqldbServer {
    public static void main( String[] args ) {
        Server memdb = startMemServer( "memdb" );
        
        Runtime.getRuntime().addShutdownHook( new Thread( () -> {
           stopServer( memdb );
        } ) );
    }
    /*
    jdbc:hsqldb:hsql://localhost:9001/memDb
    - or -
    jdbc:hsqldb:mem:memDb
     */

    /*
    Server server = new Server();
    server.setDatabaseName(0, "mainDb");
    server.setDatabasePath(0, "mem:mainDb");
    server.setPort(9001); // this is the default port
    server.start();
     */
    /**
     * @throws RuntimeException if server startup failed
     */
    public static Server startMemServer( String dbName ) {
        try {
            Server server = new Server();
            server.setDatabaseName( 0, dbName );
            server.setDatabasePath( 0, "mem:" + dbName );
            server.start();
            return server;
        } catch ( Exception e ) {
            throw new RuntimeException( "Could not start HSQLDB server", e );
        }
    }

    /**
     * @throws RuntimeException if server startup failed
     */
    public static Server startMemServer( String dbName, int port ) {
        try {
            Server server = new Server();
            server.setDatabaseName( 0, dbName );
            server.setDatabasePath( 0, "mem:" + dbName );
            server.setPort( port );
            server.start();
            return server;
        } catch ( Exception e ) {
            throw new RuntimeException( "Could not start HSQLDB server", e );
        }
    }

    public static void stopServer( Server server ) {
        server.shutdown();
    }
}