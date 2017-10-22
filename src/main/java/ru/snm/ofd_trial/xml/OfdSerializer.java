package ru.snm.ofd_trial.xml;

import ru.snm.ofd_trial.domain.common.OfdResponse;

/**
 * @author snm
 */
@FunctionalInterface
public interface OfdSerializer {

    String serialize( OfdResponse response );
}
