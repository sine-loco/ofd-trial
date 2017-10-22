package ru.snm.ofd_trial;

import com.jolbox.bonecp.BoneCPDataSource;
import org.flywaydb.core.Flyway;
import ru.snm.ofd_trial.domain.common.OfdCustomerAction;
import ru.snm.ofd_trial.xml.OfdDeserializer;
import ru.snm.ofd_trial.xml.OfdSerializer;

import javax.sql.DataSource;

/**
 * Global context.
 * Must be accessible from every part of the application.
 *
 * @author snm
 */
public final class OfdGlobalContext {
    private static volatile OfdGlobalContext instance = null;

    private final OfdDeserializer deserializer;
    private final OfdSerializer serializer;
    private final OfdCustomerAction service;

    private final Flyway flyway;

    private final BoneCPDataSource dataSource;

    private final OfdConfig config;

    /**
     * Initialize global context.
     *
     * @throws IllegalStateException If context has been initialized already
     */
    public static synchronized void initContext( OfdDeserializer deserializer,
            OfdSerializer serializer, OfdCustomerAction service,
            BoneCPDataSource datasource, Flyway flyway, OfdConfig config )
            throws IllegalStateException
    {
        if ( instance != null ) {
            throw new IllegalStateException( "Global context is already initialized" );
        }
        instance = new OfdGlobalContext(
                deserializer, serializer, service, datasource, flyway, config );
    }

    public static synchronized void clearContext() {
        if ( instance == null ) { return; }
        try {
            instance.dataSource.close();
        } finally {
            instance = null;
        }

    }

    /**
     * Get instance of a context.
     * Call it after {@code initContext(...)}
     *
     * @throws IllegalStateException If context has not been initialized.
     */
    public static OfdGlobalContext getContext() throws IllegalStateException {
        OfdGlobalContext local = OfdGlobalContext.instance;
        if ( local == null ) {
            throw new IllegalStateException( "Global context is not initialized" );
        }
        return local;
    }

    private OfdGlobalContext( OfdDeserializer deserializer, OfdSerializer serializer,
            OfdCustomerAction service,
            BoneCPDataSource datasource, Flyway flyway, OfdConfig config )
    {
        this.deserializer = deserializer;
        this.serializer = serializer;
        this.service = service;
        this.flyway = flyway;
        this.config = config;
        this.dataSource = datasource;
    }



    public OfdDeserializer getDeserializer() { return deserializer; }

    public OfdSerializer getSerializer() { return serializer; }

    public OfdCustomerAction getService() { return service; }

    public DataSource getDataSource() { return dataSource; }
}
