package ru.snm.ofd_trial;

/**
 * @author snm
 */
public final class OfdConfig {

    private final static int DEFAULT_PORT = 8080;

    private final static String DEFAULT_PATH = "/ofd";

    private static volatile OfdConfig INSTANSE = null;

    private OfdConfig() {}

    public static synchronized OfdConfig getConfig() {
        if ( INSTANSE == null ) {
            INSTANSE = new OfdConfig();
        }
        return INSTANSE;
    }

    public int getPort() {
        // fixme implement
        return DEFAULT_PORT;
    }

    public String getPath() {
        // fixme implement
        return DEFAULT_PATH;
    }
}
