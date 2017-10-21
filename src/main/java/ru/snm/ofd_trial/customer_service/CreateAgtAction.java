package ru.snm.ofd_trial.customer_service;

import ru.snm.ofd_trial.xml.OfdRequest;
import ru.snm.ofd_trial.xml.OfdResponse;

/**
 * @author snm
 */
abstract class CreateAgtAction {
    private CreateAgtAction() {}

    static OfdResponse createAgt( OfdRequest request ) {
        // fixme implement
        return OfdCustomerFacade.TECH_ERROR_RESPONSE;
    }
}
