package ru.snm.ofd_trial.xml;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

/**
 * @author snm
 */
public abstract class SimpleXmlFunctions {

    private static final Persister persister =
            new Persister( new Format( "<?xml version=\"1.0\" encoding=\"utf-8\"?>" ) );

    private SimpleXmlFunctions() {}

    public static OfdRequest deserialize( String xml )
            throws OfdDeserializationException
    {
        try {
            return persister.read( OfdRequest.class, xml );
        } catch ( Exception e ) {
            throw new OfdDeserializationException(
                    "Could not deserialize [" + xml + "]", e );
        }
    }

    public static String serialize( OfdResponse response )
            throws OfdSerializationException
    {
        StringWriter writer = new StringWriter();
        try {
            persister.write( response, writer );
            return writer.toString();
        } catch ( Exception e ) {
            throw new OfdSerializationException(
                    "could not serialize OfdResponse [" + response + "]", e );
        }
    }

    public static String extraToString( Map<String, String> extra ) {
        StringBuilder result = new StringBuilder();
        result.append( "{" );
        Iterator<Map.Entry<String, String>> iterator = extra.entrySet().iterator();
        String key;
        Map.Entry<String, String> next;
        for (;;) {
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
            } else {
                break;
            }
        }
        result.append( "}" );
        return result.toString();
    }
}
