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
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;

@ConditionalOnClass(Jackson2ObjectMapperBuilder.class)
@Configuration
public class JsonConfiguration {
    /**
     * Teaches Jackson to serialize {@link Instant} globally according to a
     * <var>dateFormat</var> and <var>timeZone</var>, defaulting respectively
     * to <em>ISO_INSTANT</em> and <em>UTC</em>, and mapping them from the
     * matching Spring application properties, {@code spring.jackson
     * .date-format} and {@code spring.jackson.timze-zone}.
     * <p>
     * If <var>timeZone</var> is not a valid "pattern" for {@link
     * DateTimeFormatter}, treats it as a <em>symbolic name</em> for the
     * pre-defined constants therein, <em>eg</em>, "RFC_1123_DATE_TIME" for
     * {@link DateTimeFormatter#RFC_1123_DATE_TIME}.
     */
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
                                + "ISO_INSTANT format does not use timeZone");
            }

            final var formatter = formatterFor(format);
            final var thisTimeZone = null == timeZone
                    ? TimeZone.getTimeZone("UTC") : timeZone;

            this.formatter = formatter
                    .withZone(thisTimeZone.toZoneId().normalized());
        }

        private static DateTimeFormatter formatterFor(final String format) {
            if (null == format) return ISO_INSTANT;
            try {
                return DateTimeFormatter.ofPattern(format);
            } catch (final IllegalArgumentException badFormat) {
                final var field = findField(DateTimeFormatter.class, format,
                        DateTimeFormatter.class);
                if (null == field) throw badFormat;
                return (DateTimeFormatter) getField(field, null);
            }
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
