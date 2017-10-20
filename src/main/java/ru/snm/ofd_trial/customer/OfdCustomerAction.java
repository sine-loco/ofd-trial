package ru.snm.ofd_trial.customer;

import ru.snm.ofd_trial.xml.OfdRequest;
import ru.snm.ofd_trial.xml.OfdResponse;

/**
 * @author snm
 */
@FunctionalInterface
public interface OfdCustomerAction {

    OfdResponse processRequest( OfdRequest request );
}
