package ru.snm.ofd_trial.domain.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.snm.ofd_trial.domain.account.CreateAgtAction;
import ru.snm.ofd_trial.domain.account.GetBalanceAction;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Root logic for all account operations.
 *
 * @author snm
 */
public class OfdCustomerFacade {
    private final static Logger logger = LogManager.getLogger();

    public static OfdResponse processRequest( OfdRequest request ) {
        logger.trace( "facade requested: {}", request );
        return AccountAction.actionFor( request ).processRequest( request );
    }


    private enum AccountAction implements OfdCustomerAction {
        CREATE_AGT ( "CREATE-AGT", CreateAgtAction::createAgt ),

        GET_BALANCE ( "GET-BALANCE", GetBalanceAction::processRequest ),

        UNSUPPORTED ( "", UnsupportedAction::reportUnsupported );


        private static Map<String, AccountAction> mapping;

        static {
            mapping = Arrays.stream( AccountAction.values() )
                    .filter( action -> action != UNSUPPORTED )
                    .collect( toMap( action -> action.rqType, action -> action ) );
        }


        private final String rqType;
        private final OfdCustomerAction action;

        AccountAction( String rqType, OfdCustomerAction action ) {
            this.rqType = rqType;
            this.action = action;
        }

        public static AccountAction actionFor( OfdRequest request ) {
            return mapping.getOrDefault(
                    request.requestType.toUpperCase(), UNSUPPORTED );
        }

        @Override
        public final OfdResponse processRequest( OfdRequest request ) {
            logger.trace( "action [{}] requested: {}", this, request );
            return action.processRequest( request );
        }
    }
}
