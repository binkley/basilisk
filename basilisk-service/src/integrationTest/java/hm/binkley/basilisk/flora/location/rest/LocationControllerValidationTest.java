package hm.binkley.basilisk.flora.location.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.ProblemWebMvcTest;
import hm.binkley.basilisk.flora.location.Locations;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfiguration.class)
@ProblemWebMvcTest(LocationController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class LocationControllerValidationTest {
    private static final String violationField = "$.violations[0].field";
    private static final String violationMessage = "$.violations[0].message";
    private static final String violationStatus = "$.status";

    private final MockMvc problemMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Locations locations;

    @SuppressFBWarnings("RV")
    @Test
    void shouldRejectShortNames()
            throws Exception {
        problemMvc.perform(get("/locations/with-name/F"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(violationField,
                        equalTo("getByName.name")))
                .andExpect(jsonPath(violationMessage,
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath(violationStatus,
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(locations);
    }

    @Test
    void shouldRejectShortRequestNames()
            throws Exception {
        problemMvc.perform(post("/locations")
                .content(asJson(LocationRequest.builder()
                        .name("F")
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(violationField,
                        equalTo("name")))
                .andExpect(jsonPath(violationMessage,
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath(violationStatus,
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(locations);
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
