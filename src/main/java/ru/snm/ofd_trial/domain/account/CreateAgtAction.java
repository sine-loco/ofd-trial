package ru.snm.ofd_trial.domain.account;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.snm.ofd_trial.domain.common.OfdDtoHelper;
import ru.snm.ofd_trial.domain.common.OfdRequest;
import ru.snm.ofd_trial.domain.common.OfdResponse;

/**
 * @author snm
 */
public abstract class CreateAgtAction {
    private final static Logger logger = LogManager.getLogger();

    private CreateAgtAction() {}

    public static OfdResponse createAgt( OfdRequest request ) {
        return OfdDtoHelper.OK_WITH_NO_DATA;
    }

    private final static String SQL =
            "SELECT login, CASE WHEN password = ? THEN true ELSE false END " +
            "FROM ofd.account " +
            "WHERE login = ?;";
}
