package ru.snm.ofd_trial.domain.common;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Collections;
import java.util.Map;

import static ru.snm.ofd_trial.domain.common.OfdRequestTags.*;

/**
 * @author snm
 */
@Root( name = EN_REQUEST )
public class OfdRequest {
    @Element( name = EN_REQUEST_TYPE )
    public final String requestType;

    @ElementMap( entry = EN_EXTRA, key = AN_NAME_EXTRA, attribute = true, inline = true )
    final Map<String, String> extra;

    public OfdRequest(
            @Element( name = EN_REQUEST_TYPE )
                    String requestType,
            @ElementMap( entry = EN_EXTRA, key = AN_NAME_EXTRA, attribute = true,
                        inline = true )
                    Map<String, String> extra )
            throws IllegalArgumentException
    {
        this.requestType = requestType;
        if ( extra == null ) {
            throw new IllegalArgumentException( "OfdRequest#extra must not be null" );
        }
        this.extra = Collections.unmodifiableMap( extra );
    }

    @Override
    public String toString() {
        return "OfdRequest{" +
                "requestType='" + requestType + '\'' +
                ", extra=" + OfdDtoHelper.extraToString( extra ) +
                '}';
    }

}
