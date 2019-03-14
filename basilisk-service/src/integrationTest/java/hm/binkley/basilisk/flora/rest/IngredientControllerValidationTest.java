package hm.binkley.basilisk.flora.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.ProblemWebMvcTest;
import hm.binkley.basilisk.flora.domain.Ingredients;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static java.math.BigDecimal.ONE;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JsonConfiguration.class,
        WorkaroundComponentScanFindingAllConverters.class})
@ProblemWebMvcTest(IngredientController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class IngredientControllerValidationTest {
    private static final String FIRST_FIELD = "$.violations[0].field";
    private static final String FIRST_MESSAGE = "$.violations[0].message";
    private static final String STATUS = "$.status";

    private final MockMvc problemMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Ingredients ingredients;

    @SuppressFBWarnings("RV")
    @Test
    void shouldRejectShortNames()
            throws Exception {
        problemMvc.perform(get("/ingredient/find/F"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(FIRST_FIELD,
                        equalTo("getByName.name")))
                .andExpect(jsonPath(FIRST_MESSAGE,
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath(STATUS,
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(ingredients);
    }

    @Test
    void shouldRejectShortRequestNames()
            throws Exception {
        problemMvc.perform(post("/ingredient")
                .content(asJson(UnusedIngredientRequest.builder()
                        .name("F")
                        .quantity(ONE)
                        .chefId(17L)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(FIRST_FIELD,
                        equalTo("name")))
                .andExpect(jsonPath(FIRST_MESSAGE,
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath(STATUS,
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(ingredients);
    }

    @Test
    void shouldRejectMissingQuantity()
            throws Exception {
        problemMvc.perform(post("/ingredient")
                .content(asJson(UnusedIngredientRequest.builder()
                        .name("EGGS")
                        .quantity(null)
                        .chefId(17L)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(FIRST_FIELD,
                        equalTo("quantity")))
                .andExpect(jsonPath(FIRST_MESSAGE,
                        equalTo("must not be null")))
                .andExpect(jsonPath(STATUS,
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(ingredients);
    }

    @Test
    void shouldRejectMissingChefId()
            throws Exception {
        problemMvc.perform(post("/ingredient")
                .content(asJson(UnusedIngredientRequest.builder()
                        .name("EGGS")
                        .quantity(ONE)
                        .chefId(null)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(FIRST_FIELD,
                        equalTo("chefId")))
                .andExpect(jsonPath(FIRST_MESSAGE,
                        equalTo("must not be null")))
                .andExpect(jsonPath(STATUS,
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(ingredients);
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
