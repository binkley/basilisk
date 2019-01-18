package hm.binkley.basilisk.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Map;

import static java.time.ZoneOffset.UTC;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ALWAYS;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.NEVER;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@JsonWebMvcTest(BasiliskController.class)
class BasiliskControllerValidationTest {
    private static final long ID = 1L;
    private static final OffsetDateTime WHEN = OffsetDateTime.of(
            2011, 2, 3, 4, 5, 6, 7_000_000, UTC);

    @Autowired
    private MockMvc jsonMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BasiliskRepository repository;
    @MockBean
    private BasiliskService service;
    // A spy so Spring reads the real one from application.yml, and we can
    // change selected configuration values; spies are best very rare
    @SpyBean
    private ServerProperties server;
    private ErrorProperties error;

    private static Map<String, Serializable> responseMapFor(
            final String word, final String extra) {
        return Map.of(
                "id", ID,
                "word", word,
                "when", WHEN.toInstant(),
                "extra", extra);
    }

    @BeforeEach
    void setUp() {
        error = spy(server.getError());
        when(server.getError())
                .thenReturn(error);
    }

    @SuppressFBWarnings("RV")
    @Test
    void shouldRejectShortWords()
            throws Exception {
        jsonMvc.perform(get("/basilisk/find/F"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error", containsString(
                        "word: length must be between 3 and 32")))
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.status",
                        equalTo(UNPROCESSABLE_ENTITY.value())))
                .andExpect(jsonPath("$.trace").doesNotExist());

        verifyNoMoreInteractions(repository, service);
    }

    @Test
    void shouldRejectShortRequestWords()
            throws Exception {
        jsonMvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("F")
                        .when(WHEN)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error", containsString(
                        "on field 'word': rejected value [F]")))
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.status",
                        equalTo(UNPROCESSABLE_ENTITY.value())))
                .andExpect(jsonPath("$.trace").doesNotExist());

        verifyNoMoreInteractions(repository, service);
    }

    @Test
    void shouldRejectShortMissingWhens()
            throws Exception {
        jsonMvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error", containsString(
                        "on field 'when': rejected value [null]")))
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.status",
                        equalTo(UNPROCESSABLE_ENTITY.value())))
                .andExpect(jsonPath("$.trace").doesNotExist());

        verifyNoMoreInteractions(repository, service);
    }

    @Test
    void shouldNotShowStackTraceWhenForbidden()
            throws Exception {
        when(error.getIncludeStacktrace())
                .thenReturn(NEVER);

        jsonMvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build()))
                .param("trace", "true"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.trace").doesNotExist());

        verifyNoMoreInteractions(repository, service);
    }

    @Test
    void shouldShowStackTraceWhenRequested()
            throws Exception {
        when(error.getIncludeStacktrace())
                .thenReturn(ON_TRACE_PARAM);

        jsonMvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build()))
                .param("trace", "true"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.trace").exists());

        jsonMvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build()))
                .param("trace", "false"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.trace").doesNotExist());

        verifyNoMoreInteractions(repository, service);
    }

    @Test
    void shouldShowStackTraceWhenRequired()
            throws Exception {
        when(error.getIncludeStacktrace())
                .thenReturn(ALWAYS);

        jsonMvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.trace").exists());

        verifyNoMoreInteractions(repository, service);
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
