package ru.snm.ofd_trial.customer_service;

import ru.snm.ofd_trial.xml.OfdRequest;
import ru.snm.ofd_trial.xml.OfdResponse;

/**
 * @author snm
 */
abstract class UnsupportedAction {
    private UnsupportedAction() {}

    static OfdResponse reportUnsupported( OfdRequest request ) {
        String type = request.requestType;
        throw new OfdUnsupportedActionException(
                "Request type [" + type + "] is currently not supported" );
    }
}
