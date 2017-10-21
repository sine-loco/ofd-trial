package ru.snm.ofd_trial.customer_service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.snm.ofd_trial.xml.OfdRequest;
import ru.snm.ofd_trial.xml.OfdResponse;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Root logic for all customer_service operations.
 *
 * @author snm
 */
public class OfdCustomerFacade {
    private final static Logger logger = LogManager.getLogger();

    // smells like a leaked abstraction. A bit, maybe :)
    /** Response for any technical error */
    public final static OfdResponse TECH_ERROR_RESPONSE = new OfdResponse( 2 );

    public static OfdResponse processRequest( OfdRequest request ) {
        logger.trace( "facade requested: {}", request );
        return Action.actionFor( request ).processRequest( request );
    }


    private enum Action implements OfdCustomerAction {
        CREATE_AGT ( "create-agt", CreateAgtAction::createAgt ),

        GET_BALANCE ( "get-balance", GetBalanceAction::processRequest ),

        UNSUPPORTED ( "", UnsupportedAction::reportUnsupported );


        private static Map<String, Action> mapping;

        static {
            mapping = Arrays.stream( Action.values() )
                    .filter( action -> action != UNSUPPORTED )
                    .collect( toMap( action -> action.rqType, action -> action ) );
        }


        private final String rqType;
        private final OfdCustomerAction action;

        Action( String rqType, OfdCustomerAction action ) {
            this.rqType = rqType;
            this.action = action;
        }

        public static Action actionFor( OfdRequest request ) {
            return mapping.getOrDefault(
                    request.requestType.toLowerCase(), UNSUPPORTED );
        }

        @Override
        public final OfdResponse processRequest( OfdRequest request ) {
            logger.trace( "action [{}] requested: {}", this, request );
            return action.processRequest( request );
        }
    }
}
