package ru.snm.ofd_trial.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.Collections;
import java.util.Map;

import static ru.snm.ofd_trial.xml.OfdResponseTags.*;
import static ru.snm.ofd_trial.xml.SimpleXmlFunctions.extraToString;

/**
 * @author snm
 */
@Root( name = EN_RESPONSE )
public class OfdResponse {
    @Element( name = EN_RESULT_CODE )
    final int resultCode;

    @ElementMap( entry = EN_EXTRA, key = AN_NAME_EXTRA,
            attribute = true, inline = true, required = false )
    final Map<String, String> extra;

    /**
     * For result-code with extra.
     *
     * @param extra nust not be null.
     *     If no extra is needed, use {@linkplain OfdResponse#OfdResponse(int)}
     * @throws IllegalArgumentException If extra is {@code null}
     */
    public OfdResponse( int resultCode, Map<String, String> extra )
        throws IllegalArgumentException
    {
        this.resultCode = resultCode;
        if ( extra == null ) {
            throw new IllegalArgumentException( "OfdResponse#extra must not be null" );
        }
        this.extra = Collections.unmodifiableMap( extra );
    }

    /** For result-code only. */
    public OfdResponse( int resultCode ) {
        this( resultCode, Collections.emptyMap() );
    }

    @Override
    public String toString() {
        return "OfdResponse{" +
                "resultCode=" + resultCode +
                ", extra=" + extraToString( extra ) +
                '}';
    }
}
