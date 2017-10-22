package ru.snm.ofd_trial.domain.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.snm.ofd_trial.xml.SimpleXmlFunctions;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static ru.snm.ofd_trial.domain.common.OfdResponseTags.*;

/**
 * Tests serialization and deserialization of DTOs.
 *
 * @author snm
 */
class DtoXmlTest {
    @Tag( "unit" )
    @ParameterizedTest
    @CsvFileSource(resources = "/xml/valid-req.csv")
    @DisplayName( "deserialization of a valid request" )
    void deserialize_valid( String xml ) {
        OfdRequest rq = SimpleXmlFunctions.deserialize( xml );
    }

    @Tag( "unit" )
    @Test
    @DisplayName( "serialization of a valid response" )
    void serialize_valid() {
        final String KEY_1 = "k-1";
        final String KEY_2 = "k-2";
        final String VAL_1 = "v-1";
        final String VAL_2 = "v-2";
        final int RESULT_CODE = 0;

        Map<String, String> map = new HashMap<>();
        map.put( KEY_1, VAL_1 );
        map.put( KEY_2, VAL_2 );

        OfdResponse rsWithExtra = new OfdResponse( RESULT_CODE, map );
        OfdResponse rsWithoutExtra = new OfdResponse( RESULT_CODE );

        Assertions.assertAll( "serialization",
                () -> {
                    String xml = SimpleXmlFunctions.serialize( rsWithExtra );

                    Assertions.assertAll( "with extra: all tags are present",
                            () -> Assertions.assertTrue( xml.contains( asXml( EN_RESPONSE ) ),
                                    EN_RESPONSE ),
                            () -> Assertions.assertTrue( xml.contains( asXml( EN_RESULT_CODE ) ),
                                    EN_RESULT_CODE ),
                            () -> Assertions.assertTrue( xml.contains( "<" + EN_EXTRA ) ,
                                    EN_EXTRA ),
                            () -> Assertions.assertTrue( xml.contains( AN_NAME_EXTRA ),
                                    AN_NAME_EXTRA )
                    );

                    Assertions.assertAll( "all values are serialized",
                            () -> Assertions.assertTrue( xml.contains( KEY_1 ) ),
                            () -> Assertions.assertTrue( xml.contains( KEY_2 ) ),
                            () -> Assertions.assertTrue( xml.contains( VAL_1 ) ),
                            () -> Assertions.assertTrue( xml.contains( VAL_2 ) ),
                            () -> Assertions.assertTrue( xml.contains( valueOf( RESULT_CODE ) ) ) );
                },
                () -> {
                    String xml = SimpleXmlFunctions.serialize( rsWithoutExtra );

                    Assertions.assertAll( "without extra: only required tags are present",
                            () -> Assertions.assertTrue( xml.contains( asXml( EN_RESPONSE ) ) ),
                            () -> Assertions.assertTrue( xml.contains( asXml( EN_RESULT_CODE ) ) ),
                            () -> Assertions.assertFalse( xml.contains( "<" + EN_EXTRA ) )
                    );

                    Assertions.assertTrue( xml.contains( valueOf( RESULT_CODE ) ),
                            "result-code is serialized" );
                }
        );
    }

    static String asXml( String tag ) {
        return "<" + tag + ">";
    }
}