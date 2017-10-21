package ru.snm.ofd_trial;

/**
 * "Marker" root exception for all custom ones.
 *
 * @author snm
 */
public abstract class OfdException extends RuntimeException {
    public OfdException( String message ) {
        super( message );
    }

    public OfdException( String message, Throwable cause ) {
        super( message, cause );
    }

    protected OfdException( String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace )
    {
        super( message, cause, enableSuppression, writableStackTrace );
    }
}
