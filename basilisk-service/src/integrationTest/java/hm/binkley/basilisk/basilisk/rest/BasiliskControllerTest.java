package hm.binkley.basilisk.basilisk.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.basilisk.domain.Basilisk;
import hm.binkley.basilisk.basilisk.domain.Basilisks;
import hm.binkley.basilisk.basilisk.domain.store.BasiliskRecord;
import hm.binkley.basilisk.basilisk.domain.store.CockatriceRecord;
import hm.binkley.basilisk.basilisk.service.BasiliskService;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.domain.Ingredients;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.math.BigDecimal.TEN;
import static java.time.Instant.EPOCH;
import static java.time.ZoneOffset.UTC;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfiguration.class)
@JsonWebMvcTest(
        controllers = BasiliskController.class // ,
//        excludeFilters = @Filter(
//                type = REGEX, pattern = "^(?!hm.binkley.basilisk.basilisk)")
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BasiliskControllerTest {
    private static final long BASILISK_ID = 1L;
    private static final Instant AT = OffsetDateTime.of(
            2011, 2, 3, 14, 5, 6, 0, UTC)
            .toInstant();
    private static final long COCKATRICE_ID = 3L;
    private static final BigDecimal BEAK_SIZE = TEN;
    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private Basilisks basilisks;
    @MockBean
    private BasiliskService service;

    @MockBean
    private Ingredients thisIsSad;

    private static String endpointWithId() {
        return "/basilisk/" + BASILISK_ID;
    }

    private static Map<String, Object> responseMapWithNoCockatricesFor(
            final String word, final String extra) {
        return Map.of(
                "id", BASILISK_ID,
                "word", word,
                "at", AT,
                "extra", extra,
                "cockatrices", List.of());
    }

    private static Map<String, Object> responseMapWithSomeCockatricesFor(
            final String word, final String extra) {
        return Map.of(
                "id", BASILISK_ID,
                "word", word,
                "at", AT,
                "extra", extra,
                "cockatrices", List.of(Map.of(
                        "id", COCKATRICE_ID,
                        "beak-size", BEAK_SIZE
                )));
    }

    @Test
    void shouldFindAll()
            throws Exception {
        final String word = "foo";
        final String extra = "Bob Barker";

        when(basilisks.all())
                .thenReturn(Stream.of(new Basilisk(
                        new BasiliskRecord(BASILISK_ID, EPOCH, word, AT)
                                .add(new CockatriceRecord(COCKATRICE_ID,
                                        EPOCH.plusSeconds(1), BEAK_SIZE)))));
        when(service.extra(word))
                .thenReturn(extra);

        jsonMvc.perform(get("/basilisk"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(List.of(
                        responseMapWithSomeCockatricesFor(word, extra)))));
    }

    @Test
    void shouldFindExplicitly()
            throws Exception {
        final String word = "BIRD";
        final String extra = "Howard";

        when(basilisks.byId(BASILISK_ID))
                .thenReturn(Optional.of(new Basilisk(
                        new BasiliskRecord(BASILISK_ID, EPOCH, word, AT)
                                .add(new CockatriceRecord(COCKATRICE_ID,
                                        EPOCH.plusSeconds(1), BEAK_SIZE)))));
        when(service.extra(word))
                .thenReturn(extra);

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        responseMapWithSomeCockatricesFor(word, extra))));
    }

    @Test
    void shouldNotFindExplicitly()
            throws Exception {
        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAcceptWords()
            throws Exception {
        final String word = "foo";
        final String extra = "Margaret Hamilton";

        when(basilisks.byWord(word))
                .thenReturn(Stream.of(new Basilisk(
                        new BasiliskRecord(BASILISK_ID, EPOCH, word, AT))));
        when(service.extra(word))
                .thenReturn(extra);

        jsonMvc.perform(get("/basilisk/find/" + word))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(List.of(
                        responseMapWithNoCockatricesFor(word, extra)))));
    }

    @Test
    void shouldAddRecordWithNoCockatrices()
            throws Exception {
        final var word = "FOO";
        final var extra = "Alice";
        final var record = BasiliskRecord.raw(word, AT);
        final BasiliskRequest request = BasiliskRequest.builder()
                .word(word)
                .at(AT)
                .build();

        when(basilisks.create(request))
                .thenReturn(new Basilisk(new BasiliskRecord(BASILISK_ID,
                        Instant.ofEpochSecond(1_000_000),
                        record.getWord(), record.getAt())));
        when(service.extra(word))
                .thenReturn(extra);

        jsonMvc.perform(post("/basilisk")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(
                        responseMapWithNoCockatricesFor(word, extra))));
    }

    @Test
    void shouldAddRecordWithSomeCockatrices()
            throws Exception {
        final var word = "FOO";
        final var extra = "Alice";
        final var cockatriceRecord = CockatriceRecord.raw(BEAK_SIZE);
        final var record = BasiliskRecord.raw(word, AT)
                .add(cockatriceRecord);
        final BasiliskRequest request = BasiliskRequest.builder()
                .word(record.getWord())
                .at(record.getAt())
                .cockatrices(List.of(CockatriceRequest.builder()
                        .beakSize(cockatriceRecord.getBeakSize())
                        .build()))
                .build();

        when(basilisks.create(request))
                .thenReturn(new Basilisk(new BasiliskRecord(BASILISK_ID,
                        Instant.ofEpochSecond(1_000_000),
                        record.getWord(), record.getAt())
                        .add(new CockatriceRecord(COCKATRICE_ID,
                                Instant.ofEpochSecond(1_000_001),
                                cockatriceRecord.getBeakSize()))));
        when(service.extra(word))
                .thenReturn(extra);

        jsonMvc.perform(post("/basilisk")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(
                        responseMapWithSomeCockatricesFor(word, extra))));
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
