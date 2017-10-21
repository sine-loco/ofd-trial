package ru.snm.ofd_trial.xml;

import ru.snm.ofd_trial.OfdException;

/**
 * @author snm
 */
public class OfdSerializationException extends OfdException {
    OfdSerializationException( String message, Throwable cause ) {
        super( message, cause, true, false );
    }
}
