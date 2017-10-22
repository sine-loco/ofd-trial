package ru.snm.ofd_trial.xml;

import ru.snm.ofd_trial.domain.common.OfdRequest;

/**
 * @author snm
 */
@FunctionalInterface
public interface OfdDeserializer {

    OfdRequest deserialize( String xml );
}
