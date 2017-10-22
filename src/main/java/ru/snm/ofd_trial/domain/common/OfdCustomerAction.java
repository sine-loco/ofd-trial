package ru.snm.ofd_trial.domain.common;

/**
 * @author snm
 */
@FunctionalInterface
public interface OfdCustomerAction {

    OfdResponse processRequest( OfdRequest request );
}
