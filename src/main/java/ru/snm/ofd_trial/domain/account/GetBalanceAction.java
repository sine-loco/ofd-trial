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

import static ru.snm.ofd_trial.domain.common.OfdDtoHelper.getLoginFrom;
import static ru.snm.ofd_trial.domain.common.OfdDtoHelper.getPasswordFrom;

/**
 * @author snm
 */
public abstract class GetBalanceAction {
    private final static Logger logger = LogManager.getLogger();

    private GetBalanceAction() {}

    public static OfdResponse processRequest( OfdRequest request ) {
        OfdResponse response;
        DataSource dataSource = OfdGlobalContext.getContext().getDataSource();

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement ps = conn.prepareStatement( SQL )
        ) {
            String login = getLoginFrom( request );
            String password = getPasswordFrom( request );

            ps.setString( 1, password );
            ps.setString( 2, login );
            try ( ResultSet rs = ps.executeQuery() ) {
                if ( rs.next() ) {
                    if ( rs.getBoolean( "password_matches" ) ) {
                        String balance = rs.getBigDecimal( "balance" ).toPlainString();
                        response = OfdDtoHelper.ok().balance( balance ).build();
                    } else {
                        response = OfdDtoHelper.WRONG_PASSWORD;
                    }
                } else {
                    response = OfdDtoHelper.NO_USER;
                }
            }
        } catch ( SQLException | RuntimeException e ) {
            logger.error( "Could not get balance", e );
            response = OfdDtoHelper.TECH_ERROR;
        }
        logger.debug( "get balance response: {}", response );

        return response;
    }



    private final static String SQL =
            "SELECT " +
                "CASE WHEN password = ? THEN true ELSE false END AS password_matches, " +
                "balance " +
            "FROM ofd.account " +
            "WHERE login = ?;";
}
