package ru.snm.ofd_trial.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.valueOf;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.snm.ofd_trial.xml.OfdResponseTags.*;

/**
 * @author snm
 */
class XmlTest {
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

        assertAll( "serialization",
                () -> {
                    String xml = SimpleXmlFunctions.serialize( rsWithExtra );

                    assertAll( "with extra: all tags are present",
                            () -> assertTrue( xml.contains( asXml( EN_RESPONSE ) ),
                                    EN_RESPONSE ),
                            () -> assertTrue( xml.contains( asXml( EN_RESULT_CODE ) ),
                                    EN_RESULT_CODE ),
                            () -> assertTrue( xml.contains( "<" + EN_EXTRA ) ,
                                    EN_EXTRA ),
                            () -> assertTrue( xml.contains( AN_NAME_EXTRA ),
                                    AN_NAME_EXTRA )
                    );

                    assertAll( "all values are serialized",
                            () -> assertTrue( xml.contains( KEY_1 ) ),
                            () -> assertTrue( xml.contains( KEY_2 ) ),
                            () -> assertTrue( xml.contains( VAL_1 ) ),
                            () -> assertTrue( xml.contains( VAL_2 ) ),
                            () -> assertTrue( xml.contains( valueOf( RESULT_CODE ) ) ) );
                },
                () -> {
                    String xml = SimpleXmlFunctions.serialize( rsWithoutExtra );

                    assertAll( "without extra: only required tags are present",
                            () -> assertTrue( xml.contains( asXml( EN_RESPONSE ) ) ),
                            () -> assertTrue( xml.contains( asXml( EN_RESULT_CODE ) ) ),
                            () -> assertFalse( xml.contains( "<" + EN_EXTRA ) )
                    );

                    assertTrue( xml.contains( valueOf( RESULT_CODE ) ),
                            "result-code is serialized" );
                }
        );
    }

    static String asXml( String tag ) {
        return "<" + tag + ">";
    }

    static Stream<Arguments> deserialization() {
        return Stream.of(
                Arguments.of()
                );
    }

}