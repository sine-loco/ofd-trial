package ru.snm.ofd_trial.domain.common;

/**
 * @author snm
 */
public abstract class UnsupportedAction {
    private UnsupportedAction() {}

     public static OfdResponse reportUnsupported( OfdRequest request ) {
        String type = request.requestType;
        throw new OfdUnsupportedActionException(
                "Request type [" + type + "] is currently not supported" );
    }
}
