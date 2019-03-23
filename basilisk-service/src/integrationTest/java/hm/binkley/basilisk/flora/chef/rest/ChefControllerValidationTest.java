package hm.binkley.basilisk.flora.chef.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.ProblemWebMvcTest;
import hm.binkley.basilisk.flora.chef.Chefs;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfiguration.class)
@ProblemWebMvcTest(ChefController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ChefControllerValidationTest {
    private static final String violationField = "$.violations[0].field";
    private static final String violationMessage = "$.violations[0].message";
    private static final String violationStatus = "$.status";

    private final MockMvc problemMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Chefs chefs;

    @SuppressFBWarnings("RV")
    @Test
    void shouldRejectShortCodes()
            throws Exception {
        problemMvc.perform(get("/chef/with-code/F"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(violationField,
                        equalTo("getByCode.code")))
                .andExpect(jsonPath(violationMessage,
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath(violationStatus,
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(chefs);
    }

    @SuppressFBWarnings("RV")
    @Test
    void shouldRejectShortNames()
            throws Exception {
        problemMvc.perform(get("/chef/with-name/F"))
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

        verifyNoMoreInteractions(chefs);
    }

    @Test
    void shouldRejectShortRequestCodes()
            throws Exception {
        problemMvc.perform(post("/chef")
                .content(asJson(ChefRequest.builder()
                        .code("F")
                        .name(CHEF_NAME)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(violationField,
                        equalTo("code")))
                .andExpect(jsonPath(violationMessage,
                        equalTo("length must be between 3 and 8")))
                .andExpect(jsonPath(violationStatus,
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(chefs);
    }

    @Test
    void shouldRejectShortRequestNames()
            throws Exception {
        problemMvc.perform(post("/chef")
                .content(asJson(ChefRequest.builder()
                        .code(CHEF_CODE)
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

        verifyNoMoreInteractions(chefs);
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
