package hm.binkley.basilisk.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializerBase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

@ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
@Configuration
public class JsonConfiguration {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer instantFormat(
            final JacksonProperties jackson) {
        return builder -> builder.serializerByType(Instant.class,
                new InstantFormatSerializer(
                        jackson.getDateFormat(), jackson.getTimeZone()));
    }

    /** @todo Figure out how to use {@link InstantSerializerBase} */
    private static class InstantFormatSerializer
            extends StdScalarSerializer<Instant> {
        private final DateTimeFormatter formatter;

        private InstantFormatSerializer(final String format,
                final TimeZone timeZone) {
            super(Instant.class);

            if (null == format && null != timeZone) {
                throw new IllegalArgumentException(
                        "Setting timeZone without dateFormat: default "
                                + "ISO_INSTANT format ignores timeZone");
            }

            final var formatter = null == format
                    ? ISO_INSTANT : DateTimeFormatter.ofPattern(format);
            final var thisTimeZone = null == timeZone
                    ? TimeZone.getTimeZone("UTC") : timeZone;

            this.formatter = formatter
                    .withZone(thisTimeZone.toZoneId().normalized());
        }

        @Override
        public void serialize(final Instant value,
                final JsonGenerator gen,
                final SerializerProvider provider)
                throws IOException {
            gen.writeString(formatter.format(value));
        }
    }
}
