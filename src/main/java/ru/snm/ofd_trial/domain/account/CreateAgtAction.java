package ru.snm.ofd_trial.domain.account;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.snm.ofd_trial.OfdGlobalContext;
import ru.snm.ofd_trial.domain.common.OfdDtoHelper;
import ru.snm.ofd_trial.domain.common.OfdRequest;
import ru.snm.ofd_trial.domain.common.OfdResponse;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ru.snm.ofd_trial.domain.common.OfdDtoHelper.*;

/**
 * @author snm
 */
public abstract class CreateAgtAction {
    private final static Logger logger = LogManager.getLogger();

    private CreateAgtAction() {}

    public static OfdResponse createAgt( OfdRequest request ) {
        OfdResponse response;
        DataSource dataSource = OfdGlobalContext.getContext().getDataSource();

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement( SQL )
        ) {
            String login = getLoginFrom( request );
            String password = getPasswordFrom( request );

            ps.setString( 1, login );
            ps.setString( 2, password );
            ps.setString( 3, login );
            int count = ps.executeUpdate();
            logger.trace( "rows affected: {}", count );
            if ( count > 0 ) {
                 response = OK_WITH_NO_DATA;
            } else {
                response = USER_ALREADY_EXISTS;
            }

        } catch ( SQLException | RuntimeException e ) {
            logger.error( "Could not create agt", e );
            response = TECH_ERROR;
        }
        logger.debug( "create agt response: {}", response );

        return response;
    }


    private final static String SQL =
            "INSERT INTO ofd.account ( login, password ) " +
            "SELECT ( * ) FROM ( VALUES ( ?, ? ) ) " +
            "WHERE NOT EXISTS ( " +
                "SELECT login FROM ofd.account WHERE login = ?" +
            ");";
}
