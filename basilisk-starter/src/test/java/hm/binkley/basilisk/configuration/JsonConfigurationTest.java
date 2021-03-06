package hm.binkley.basilisk.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class JsonConfigurationTest {
    @Mock
    private final Jackson2ObjectMapperBuilder builder;
    @Mock
    private final JsonGenerator gen;

    @Captor
    private ArgumentCaptor<JsonSerializer<Instant>> serializer;

    @Test
    void shouldFormatDefault()
            throws IOException {
        assertFormatInstant(null, null,
                "2011-02-03T14:05:06.007Z");
    }

    @Test
    void shouldFormatWithDateFormatOnly()
            throws IOException {
        assertFormatInstant("yyyy-MM-dd'T'HH:mm:ssXXX", null,
                "2011-02-03T14:05:06Z");
    }

    @Test
    void shouldFormatWithSymbolicDateFormat()
            throws IOException {
        assertFormatInstant("RFC_1123_DATE_TIME", null,
                "Thu, 3 Feb 2011 14:05:06 GMT");
    }

    @Test
    void shouldFormatWithDateFormatAndTimeZone()
            throws IOException {
        assertFormatInstant("yyyy-MM-dd'T'HH:mm:ssXXX", "GMT+1",
                "2011-02-03T15:05:06+01:00");
    }

    @Test
    void shouldComplainWithTimeZoneOnly() {
        assertThatThrownBy(() ->
                assertFormatInstant(null, "GMT+1", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldComplainWithBadDateFormat() {
        assertThatThrownBy(() ->
                assertFormatInstant("NOT A VALID FORMAT", null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void assertFormatInstant(final String dateFormat,
            final String timeZone,
            final String formatted)
            throws IOException {
        final var jackson = new JacksonProperties();
        jackson.setDateFormat(dateFormat);
        jackson.setTimeZone(
                null == timeZone ? null : TimeZone.getTimeZone(timeZone));
        final var customizer = new JsonConfiguration().instantFormat(jackson);

        customizer.customize(builder);

        verify(builder)
                .serializerByType(eq(Instant.class), serializer.capture());

        serializer.getValue().serialize(ZonedDateTime.of(
                LocalDate.of(2011, 2, 3),
                LocalTime.of(14, 5, 6, 7_000_000),
                ZoneId.of("UTC").normalized())
                .toInstant(), gen, null);

        verify(gen).writeString(formatted);

        verifyNoMoreInteractions(builder, gen);
    }
}
