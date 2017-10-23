package ru.snm.ofd_trial.domain.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Manipulations with fields of request and response DTOs.
 *
 * @author snm
 */
public abstract class OfdDtoHelper {

    /** Response for case where only OK status without any additional data is needed */
    public final static OfdResponse OK_WITH_NO_DATA = new OfdResponse( 0 );
    public static final OfdResponse USER_ALREADY_EXISTS = new OfdResponse( 1 );
    /** Response for any technical error */
    public final static OfdResponse TECH_ERROR = new OfdResponse( 2 );
    public static final OfdResponse NO_USER = new OfdResponse( 3 );
    public static final OfdResponse WRONG_PASSWORD = new OfdResponse( 4 );

    private OfdDtoHelper() {}

    public static String getPasswordFrom( OfdRequest request ) {
        return request.extra.get( "password" );
    }

    public static String getLoginFrom( OfdRequest request ) {
        return request.extra.get( "login" );
    }

    public static String getBalanceFrom( OfdResponse response ) {
        return response.extra.get( "balance" );
    }


    public static OfdResponseBuilder ok() {
        return new OfdResponseBuilder( 0 );
    }

    static String extraToString( Map<String, String> extra ) {
        StringBuilder result = new StringBuilder();
        result.append( "{" );
        Iterator<Map.Entry<String, String>> iterator = extra.entrySet().iterator();
        String key;
        Map.Entry<String, String> next;
        while ( iterator.hasNext() ) {
            next = iterator.next();
            key = next.getKey();
            result.append( key ).append( "=" );
            if ( "password".equalsIgnoreCase( key ) ) {
                result.append( "***" );
            } else {
                result.append( next.getValue() );
            }
            if ( iterator.hasNext() ) {
                result.append( ", " );
            }
        }
        result.append( "}" );
        return result.toString();
    }


    public static class OfdResponseBuilder {
        private final int resultCode;
        private final Map<String, String> extra = new HashMap<>();

        private OfdResponseBuilder( int resultCode ) {
            this.resultCode = resultCode;
        }

        public OfdResponseBuilder balance( String balance ) {
            extra.put( "balance", balance );
            return this;
        }

        public OfdResponse build() {
            return new OfdResponse( resultCode, extra );
        }
    }
}
