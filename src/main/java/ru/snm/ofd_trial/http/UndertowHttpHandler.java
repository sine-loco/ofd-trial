package ru.snm.ofd_trial.http;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.snm.ofd_trial.xml.OfdDeserializer;

/**
 * @author snm
 */
public class UndertowHttpHandler implements HttpHandler {
    private final static Logger logger = LogManager.getLogger();

    final OfdDeserializer deserializer;

    public UndertowHttpHandler( OfdDeserializer deserializer ) {
        this.deserializer = deserializer;
    }

    @Override
    public void handleRequest( HttpServerExchange exchange ) throws Exception {
        logger.trace( "received" );

        HttpString requestMethod = exchange.getRequestMethod();
        logger.trace( "request method [{}]", requestMethod );



        exchange.getResponseHeaders().put( Headers.CONTENT_TYPE, "text/plain" );
        exchange.getResponseSender().send( "Hello World" );

    }
}
