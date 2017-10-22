package ru.snm.ofd_trial.xml;

import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.stream.Format;
import ru.snm.ofd_trial.domain.common.OfdRequest;
import ru.snm.ofd_trial.domain.common.OfdResponse;

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

}
