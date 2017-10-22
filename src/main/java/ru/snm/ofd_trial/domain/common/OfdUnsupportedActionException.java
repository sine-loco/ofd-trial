package ru.snm.ofd_trial.domain.common;

import ru.snm.ofd_trial.OfdException;

/**
 * @author snm
 */
public class OfdUnsupportedActionException extends OfdException {
    protected OfdUnsupportedActionException( String message ) {
        super( message, null, false, false );
    }
}
