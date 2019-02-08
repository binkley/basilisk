package hm.binkley.basilisk.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneOffset.UTC;

@Configuration
public class JsonConfiguration {
    static final InstantFormatSerializer INSTANT_FORMAT_SERIALIZER
            = new InstantFormatSerializer();
    private static final DateTimeFormatter INSTANT_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .withZone(UTC.normalized());

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer instantFormat() {
        return builder -> builder.serializerByType(
                Instant.class, INSTANT_FORMAT_SERIALIZER);
    }

    static class InstantFormatSerializer
            extends StdScalarSerializer<Instant> {
        private InstantFormatSerializer() {
            super(Instant.class);
        }

        @Override
        public void serialize(final Instant value,
                final JsonGenerator gen,
                final SerializerProvider provider)
                throws IOException {
            gen.writeString(INSTANT_FORMATTER.format(value));
        }
    }
}
