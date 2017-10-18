package ru.snm.ofd_trial.http;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;

/**
 * @author snm
 */
@Path( "" )
public class OfdCustomerHandler {
    private final static Logger logger = LogManager.getLogger();

    @POST
    @Consumes( MediaType.APPLICATION_XML )
    @Produces( MediaType.APPLICATION_XML )
    public String handleRequest( @Context Request request, String xml ) {
        logger.trace( "XML: {}", xml );


        return "received: " + xml;
    }
}
