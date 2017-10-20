package ru.snm.ofd_trial.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.snm.ofd_trial.OfdGlobalContext;
import ru.snm.ofd_trial.customer.OfdCustomerAction;
import ru.snm.ofd_trial.xml.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

import static ru.snm.ofd_trial.customer.OfdCustomerFacade.TECH_ERROR_RESPONSE;

/**
 * @author snm
 */
@Path( "" )
public class OfdCustomerHandler {
    private final static Logger logger = LogManager.getLogger();

    private final OfdDeserializer deserializer;

    private final OfdSerializer serializer;

    private final OfdCustomerAction service;

    /** The last resort. Pre-serialized response for tech-error case. */
    private final String I_AM_BROKEN_RESPONSE;

    public OfdCustomerHandler() {
        OfdGlobalContext context = OfdGlobalContext.getContext();
        this.deserializer = context.getDeserializer();
        this.serializer = context.getSerializer();
        this.service = context.getService();

        I_AM_BROKEN_RESPONSE = serializer.serialize( TECH_ERROR_RESPONSE );
    }

    @POST
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.APPLICATION_XML )
    public String handleRequest( @Context Request request, String xml ) {
        logger.trace( "XML: {}", xml );

        String result = I_AM_BROKEN_RESPONSE;

        try {
            OfdRequest rq = deserializer.deserialize( xml );

            OfdResponse rs = service.processRequest( rq );

            result = serializer.serialize( rs );
        } catch ( OfdDeserializationException e ) {
            logger.error( "Could not deserialize request", e );
            /* it may be better to return HTTP 422, but
                a) this way is more robust
                b) anyway, I have no control over HTTP codes :) */
        } catch ( OfdSerializationException e ) {
            logger.error( "Could not serialize response", e );
        } catch ( RuntimeException e ) {
            logger.error( "Unexpected error", e );
        }

        return result;
    }
}
