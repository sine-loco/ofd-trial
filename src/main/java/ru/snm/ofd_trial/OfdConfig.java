package ru.snm.ofd_trial;

import java.util.Properties;

/**
 * @author snm
 */
public final class OfdConfig {

    private final static String DEFAULT_PORT = "8080";
    private final static String DEFAULT_PATH = "/ofd";

    public static final String PROP_PORT = "port";
    public static final String PROP_PATH = "path";


    private static volatile OfdConfig INSTANSE = null;

    public final int port;
    public final String path;

    private OfdConfig( Properties properties ) {
        port = Integer.parseInt( properties.getProperty( PROP_PORT, DEFAULT_PORT ) );

        path =  properties.getProperty( PROP_PATH, DEFAULT_PATH );
    }

    /**
     *
     * @param properties loaded and valid properties
     * @return config instance
     */
    public static synchronized OfdConfig getConfig( Properties properties ) {
        if ( INSTANSE == null ) {
            INSTANSE = new OfdConfig( properties );
        }
        return INSTANSE;
    }
}
