package ru.snm.ofd_trial.customer;

import ru.snm.ofd_trial.xml.OfdRequest;
import ru.snm.ofd_trial.xml.OfdResponse;

/**
 * Business logic for all customer operations.
 *
 * @author snm
 */
public class OfdCustomerFacade {
    // smells like a leaked abstraction. A bit, maybe :)
    /** Response for any technical error */
    public final static OfdResponse TECH_ERROR_RESPONSE = new OfdResponse( 2 );

    public static OfdResponse processRequest( OfdRequest request ) {
        // fixme implement
        return TECH_ERROR_RESPONSE;
    }
}
