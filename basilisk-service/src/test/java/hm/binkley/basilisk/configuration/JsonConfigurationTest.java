package hm.binkley.basilisk.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static hm.binkley.basilisk.configuration.JsonConfiguration.INSTANT_FORMAT_SERIALIZER;
import static java.time.ZoneOffset.UTC;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class JsonConfigurationTest {
    @Mock
    private JsonGenerator gen;

    @Test
    void shouldFormatInstant()
            throws IOException {
        INSTANT_FORMAT_SERIALIZER.serialize(ZonedDateTime.of(
                LocalDate.of(2011, 2, 3),
                LocalTime.of(4, 5, 6, 7_000_000),
                UTC.normalized()).toInstant(),
                gen, null);

        verify(gen).writeString("2011-02-03T04:05:06Z");
        verifyNoMoreInteractions(gen);
    }
}
