package hm.binkley.basilisk.basilisk.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hm.binkley.basilisk.basilisk.domain.Basilisks;
import hm.binkley.basilisk.basilisk.service.BasiliskService;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.ProblemWebMvcTest;
import hm.binkley.basilisk.flora.domain.Ingredients;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfiguration.class)
@ProblemWebMvcTest(BasiliskController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BasiliskControllerValidationTest {
    private static final Instant AT = OffsetDateTime.of(
            2011, 2, 3, 14, 5, 6, 0, UTC)
            .toInstant();

    private final MockMvc problemMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Basilisks basilisks;
    @MockBean
    private BasiliskService service;

    @MockBean
    private Ingredients thisIsSad;

    @SuppressFBWarnings("RV")
    @Test
    void shouldRejectShortWords()
            throws Exception {
        problemMvc.perform(get("/basilisk/find/F"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations[0].field",
                        equalTo("findByWord.word")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath("$.status",
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(basilisks, service);
    }

    @Test
    void shouldRejectShortRequestWords()
            throws Exception {
        problemMvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("F")
                        .at(AT)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations[0].field",
                        equalTo("word")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath("$.status",
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(basilisks, service);
    }

    @Test
    void shouldRejectShortMissingWhens()
            throws Exception {
        problemMvc.perform(post("/basilisk")
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .at(null)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations[0].field",
                        equalTo("at")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("must not be null")))
                .andExpect(jsonPath("$.status",
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(basilisks, service);
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
