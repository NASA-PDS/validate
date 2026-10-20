package gov.nasa.pds.tools.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URL;
import java.util.List;

import org.junit.jupiter.api.Test;

class HashSetReferenceInfoTest {

    @Test
    void getReferencesReturnsUnmodifiableView() throws Exception {
        HashSetReferenceInfo info = new HashSetReferenceInfo();
        URL source = new URL("https://example.com/label.xml");

        info.addReference("reference-1", source);

        List<String> references = info.getReferences();

        assertEquals(List.of("reference-1"), references);
        assertThrows(
            UnsupportedOperationException.class,
            references::clear
        );

        info.addReference("reference-2", source);

        assertEquals(
            List.of("reference-1", "reference-2"),
            references
        );
    }
}
