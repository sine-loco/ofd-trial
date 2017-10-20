package ru.snm.ofd_trial.xml;

/**
 * @author snm
 */
public class OfdSerializationException extends RuntimeException {
    public OfdSerializationException( String message, Throwable cause ) {
        super( message, cause, true, false );
    }
}
