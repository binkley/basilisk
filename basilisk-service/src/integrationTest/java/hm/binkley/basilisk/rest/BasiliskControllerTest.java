package hm.binkley.basilisk.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRecord;
import hm.binkley.basilisk.store.BasiliskRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
@JsonWebMvcTest(BasiliskController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BasiliskControllerTest {
    private static final long ID = 1L;
    private static final Instant AT = OffsetDateTime.of(
            2011, 2, 3, 4, 5, 6, 0, UTC)
            .toInstant();

    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private BasiliskRepository basilisks;
    @MockBean
    private BasiliskService service;

    private static Map<String, Serializable> responseMapFor(
            final String word, final String extra) {
        return Map.of(
                "id", ID,
                "word", word,
                "at", AT,
                "extra", extra);
    }

    @Test
    void shouldFindAll()
            throws Exception {
        final String word = "foo";
        final String extra = "Bob Barker";
        final List<BasiliskRecord> found = List.of(BasiliskRecord.builder()
                .id(ID)
                .receivedAt(EPOCH)
                .word(word)
                .at(AT)
                .build());

        when(basilisks.readAll())
                .thenReturn(found.stream());
        when(service.extra(word))
                .thenReturn(extra);

        jsonMvc.perform(get("/basilisk"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        List.of(responseMapFor(word, extra)))));
    }

    @Test
    void shouldFindExplicitly()
            throws Exception {
        final long id = 1L;
        final String word = "BIRD";
        final String extra = "Howard";

        when(basilisks.findById(id))
                .thenReturn(Optional.of(BasiliskRecord.builder()
                        .id(id)
                        .receivedAt(EPOCH)
                        .word(word)
                        .at(AT)
                        .build()));
        when(service.extra(word))
                .thenReturn(extra);

        jsonMvc.perform(get("/basilisk/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        asJson(responseMapFor(word, extra))));
    }

    @Test
    void shouldNotFindExplicitly()
            throws Exception {
        final long id = 1L;

        when(basilisks.findById(id))
                .thenReturn(Optional.empty());

        jsonMvc.perform(get("/basilisk/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAcceptWords()
            throws Exception {
        final long id = 1L;
        final String word = "foo";
        final String extra = "Margaret Hamilton";

        when(basilisks.findByWord(word))
                .thenReturn(List.of(BasiliskRecord.builder()
                        .id(id)
                        .receivedAt(EPOCH)
                        .word(word)
                        .at(AT)
                        .build()));
        when(service.extra(word))
                .thenReturn(extra);

        jsonMvc.perform(get("/basilisk/find/" + word))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        asJson(List.of(responseMapFor(word, extra)))));
    }

    @Test
    void shouldAddRecords()
            throws Exception {
        final long id = 1L;
        final String word = "FOO";
        final String extra = "Alice";
        final BasiliskRecord record = BasiliskRecord.builder()
                .word(word)
                .at(AT)
                .build();

        when(basilisks.save(record))
                .thenReturn(record
                        .withId(id)
                        .withReceivedAt(Instant.ofEpochSecond(1_000_000)));
        when(service.extra(word))
                .thenReturn(extra);

        jsonMvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word(word)
                        .at(AT)
                        .build())))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        LOCATION, "/basilisk/" + id))
                .andExpect(content().json(
                        asJson(responseMapFor(word, extra))));
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
