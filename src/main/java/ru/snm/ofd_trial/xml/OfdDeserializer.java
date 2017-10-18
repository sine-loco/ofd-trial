package ru.snm.ofd_trial.xml;

/**
 * @author snm
 */
@FunctionalInterface
public interface OfdDeserializer {

    OfdRequest deserialize( String xml );
}
