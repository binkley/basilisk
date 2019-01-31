package hm.binkley.basilisk.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hm.binkley.basilisk.configuration.ProblemWebMvcTest;
import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRepository;
import hm.binkley.basilisk.store.CityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ProblemWebMvcTest(BasiliskController.class)
class BasiliskControllerValidationTest {
    private static final OffsetDateTime WHEN = OffsetDateTime.of(
            2011, 2, 3, 4, 5, 6, 7_000_000, UTC);

    @Autowired
    private MockMvc problemMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BasiliskRepository basilisks;
    @MockBean
    private CityRepository cities;
    @MockBean
    private BasiliskService service;

    @SuppressFBWarnings("RV")
    @Test
    void shouldRejectShortWords()
            throws Exception {
        problemMvc.perform(get("/basilisk/find/F"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations[0].field",
                        equalTo("getByWord.word")))
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
                        .when(WHEN)
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
                        .when(null)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations[0].field",
                        equalTo("when")))
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
