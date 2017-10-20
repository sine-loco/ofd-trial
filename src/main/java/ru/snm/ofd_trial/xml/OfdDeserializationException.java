package ru.snm.ofd_trial.xml;

/**
 * @author snm
 */
public class OfdDeserializationException extends RuntimeException {
    public OfdDeserializationException( String message, Throwable cause ) {
        super( message, cause, true, false );
    }
}
