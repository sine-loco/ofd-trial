package ru.snm.ofd_trial;

import java.util.Properties;

/**
 * @author snm
 */
public final class OfdConfig {

    private final static String DEFAULT_PORT = "8080";
    private final static String DEFAULT_PATH = "/ofd";

    /* DB_xx defaults - solely for test purpose */
    public static final String DEFAULT_DB_DRIVER = "org.hsqldb.jdbcDriver";
    public static final String DEFAULT_DB_URL = "jdbc:hsqldb:hsql://localhost:9001/memDb";
    public static final String DEFAULT_DB_USER = "SA";
    public static final String DEFAULT_DB_PASSWORD = "";

    public static final String PROP_PORT = "port";
    public static final String PROP_PATH = "path";
    public static final String PROP_DB_DRIVER = "db.driver";
    public static final String PROP_DB_URL = "db.url";
    public static final String PROP_DB_USER = "db.user";
    public static final String PROP_DB_PASSWORD = "db.password";


    public final int port;
    public final String path;

    public final String dbDriver;
    public final String dbUrl;
    public final String dbUser;
    public final String dbPassword;


    public OfdConfig( Properties properties ) {
        port = Integer.parseInt( properties.getProperty( PROP_PORT, DEFAULT_PORT ) );

        path = properties.getProperty( PROP_PATH, DEFAULT_PATH );

        dbDriver = properties.getProperty( PROP_DB_DRIVER, DEFAULT_DB_DRIVER );
        dbUrl= properties.getProperty( PROP_DB_URL, DEFAULT_DB_URL );
        dbUser = properties.getProperty( PROP_DB_USER, DEFAULT_DB_USER );
        dbPassword = properties.getProperty( PROP_DB_PASSWORD, DEFAULT_DB_PASSWORD );
    }
}
