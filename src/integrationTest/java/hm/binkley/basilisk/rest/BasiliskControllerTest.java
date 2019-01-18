package hm.binkley.basilisk.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRecord;
import hm.binkley.basilisk.store.BasiliskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static java.time.Instant.EPOCH;
import static java.time.ZoneOffset.UTC;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ALWAYS;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.NEVER;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest(BasiliskController.class)
class BasiliskControllerTest {
    private static final OffsetDateTime WHEN = OffsetDateTime.of(
            2011, 2, 3, 4, 5, 6, 7_000_000, UTC);

    @Autowired
    private WebApplicationContext ctx;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BasiliskRepository repository;
    @MockBean
    private BasiliskService service;
    @SpyBean
    private ServerProperties server;

    private MockMvc mvc;
    private ErrorProperties error;

    @BeforeEach
    void setUp() {
        mvc = assumeJson();

        error = spy(server.getError());
        when(server.getError())
                .thenReturn(error);
    }

    @Test
    void shouldPage()
            throws Exception {
        final String word = "foo";
        final Pageable pageable = PageRequest.of(0, 2);
        final List<BasiliskRecord> found = List.of(BasiliskRecord.builder()
                .id(1L)
                .receivedAt(EPOCH)
                .word(word)
                .when(WHEN.toInstant())
                .build());
        when(repository.findAll(pageable))
                .thenReturn(new PageImpl<>(found, pageable, found.size()));
        when(service.extra(word))
                .thenReturn("Bob Barker");

        mvc.perform(get("/basilisk")
                .param("page", "0")
                .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(new PageImpl<>(List.of(
                        Map.of("word", word,
                                "when", "2011-02-03T04:05:06.007Z",
                                "extra", "Bob Barker")), pageable,
                        found.size()))));
    }

    @Test
    void shouldAcceptWords()
            throws Exception {
        final String word = "foo";
        when(repository.findByWord(word))
                .thenReturn(List.of(BasiliskRecord.builder()
                        .id(1L)
                        .receivedAt(EPOCH)
                        .word(word)
                        .when(WHEN.toInstant())
                        .build()));
        when(service.extra(word))
                .thenReturn("Margaret Hamilton");

        mvc.perform(get("/basilisk/" + word))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(List.of(
                        Map.of("word", word,
                                "when", "2011-02-03T04:05:06.007Z",
                                "extra", "Margaret Hamilton")))));
    }

    @SuppressFBWarnings("RV")
    @Test
    void shouldRejectShortWords()
            throws Exception {
        mvc.perform(get("/basilisk/F"))
                .andExpect(status().isIAmATeapot());

        verifyNoMoreInteractions(repository, service);
    }

    @Test
    void shouldAddRecords()
            throws Exception {
        final String word = "FOO";
        final BasiliskRecord record = BasiliskRecord.builder()
                .word(word)
                .when(WHEN.toInstant())
                .build();
        when(repository.save(record))
                .thenReturn(record
                        .withId(1L)
                        .withReceivedAt(Instant.ofEpochSecond(1_000_000)));

        mvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word(word)
                        .when(WHEN)
                        .build())))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    void shouldRejectShortRequestWords()
            throws Exception {
        mvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("F")
                        .when(WHEN)
                        .build())))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.error", containsString(
                        "on field 'word': rejected value [F]")))
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.status",
                        equalTo(I_AM_A_TEAPOT.value())))
                .andExpect(jsonPath("$.trace").doesNotExist());

        verifyNoMoreInteractions(repository, service);
    }

    @Test
    void shouldRejectShortMissingWhens()
            throws Exception {
        mvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build())))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.error", containsString(
                        "on field 'when': rejected value [null]")))
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.status",
                        equalTo(I_AM_A_TEAPOT.value())))
                .andExpect(jsonPath("$.trace").doesNotExist());

        verifyNoMoreInteractions(repository, service);
    }

    @Test
    void shouldNotShowStackTraceWhenForbidden()
            throws Exception {
        when(error.getIncludeStacktrace())
                .thenReturn(NEVER);

        mvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build()))
                .param("trace", "true"))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.trace").doesNotExist());

        verifyNoMoreInteractions(repository, service);
    }

    @Test
    void shouldShowStackTraceWhenRequested()
            throws Exception {
        when(error.getIncludeStacktrace())
                .thenReturn(ON_TRACE_PARAM);

        mvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build()))
                .param("trace", "true"))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.trace").exists());

        mvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build()))
                .param("trace", "false"))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.trace").doesNotExist());

        verifyNoMoreInteractions(repository, service);
    }

    @Test
    void shouldShowStackTraceWhenRequired()
            throws Exception {
        when(error.getIncludeStacktrace())
                .thenReturn(ALWAYS);

        mvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build())))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.trace").exists());

        verifyNoMoreInteractions(repository, service);
    }

    private MockMvc assumeJson() {
        return webAppContextSetup(ctx)
                .defaultRequest(post("/")
                        .contentType(APPLICATION_JSON_UTF8)
                        .accept(APPLICATION_JSON_UTF8_VALUE))
                .alwaysExpect(header().string(
                        CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE))
                .build();
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
