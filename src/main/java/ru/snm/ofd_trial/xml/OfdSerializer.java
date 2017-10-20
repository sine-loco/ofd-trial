package ru.snm.ofd_trial.xml;

/**
 * @author snm
 */
@FunctionalInterface
public interface OfdSerializer {

    String serialize( OfdResponse response );
}
