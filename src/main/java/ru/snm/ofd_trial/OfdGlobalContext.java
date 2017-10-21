package ru.snm.ofd_trial;

import ru.snm.ofd_trial.customer_service.OfdCustomerAction;
import ru.snm.ofd_trial.xml.OfdDeserializer;
import ru.snm.ofd_trial.xml.OfdSerializer;

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

    /**
     * Initialize global context.
     *
     * @throws IllegalStateException If context has been initialized already
     */
    public static synchronized void initContext( OfdDeserializer deserializer,
            OfdSerializer serializer, OfdCustomerAction service )
            throws IllegalStateException
    {
        if ( instance != null ) {
            throw new IllegalStateException( "Global context is already initialized" );
        }
        instance = new OfdGlobalContext( deserializer, serializer, service );
    }

    public static synchronized void clearContext() {
        instance = null;
    }

    /**
     * Get instance of a context.
     * Call it after {@linkplain OfdGlobalContext#initContext(
     * OfdDeserializer, OfdSerializer, OfdCustomerAction)}
     *
     * @throws IllegalStateException If context has not been initialized.
     */
    public static OfdGlobalContext getContext() throws IllegalStateException {
        if ( instance == null ) {
            throw new IllegalStateException( "Global context is not initialized" );
        }
        return instance;
    }

    private OfdGlobalContext( OfdDeserializer deserializer,
            OfdSerializer serializer, OfdCustomerAction service )
    {
        this.deserializer = deserializer;
        this.serializer = serializer;
        this.service = service;
    }

    public OfdDeserializer getDeserializer() {
        return deserializer;
    }

    public OfdSerializer getSerializer() {
        return serializer;
    }

    public OfdCustomerAction getService() {
        return service;
    }
}
