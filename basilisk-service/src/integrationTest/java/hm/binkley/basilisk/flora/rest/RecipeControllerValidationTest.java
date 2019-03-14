package hm.binkley.basilisk.flora.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.ProblemWebMvcTest;
import hm.binkley.basilisk.flora.domain.Recipes;
import hm.binkley.basilisk.flora.service.SpecialService;
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

@Import({JsonConfiguration.class,
        WorkaroundComponentScanFindingAllConverters.class})
@ProblemWebMvcTest(RecipeController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class RecipeControllerValidationTest {
    private final MockMvc problemMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Recipes recipes;
    @MockBean
    private SpecialService specialService;

    @SuppressFBWarnings("RV")
    @Test
    void shouldRejectShortNames()
            throws Exception {
        problemMvc.perform(get("/recipe/find/F"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations[0].field",
                        equalTo("getByName.name")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath("$.status",
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(recipes);
    }

    @Test
    void shouldRejectShortRequestNames()
            throws Exception {
        problemMvc.perform(post("/recipe")
                .content(asJson(RecipeRequest.builder()
                        .name("F")
                        .chefId(17L)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations[0].field",
                        equalTo("name")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath("$.status",
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(recipes);
    }

    @Test
    void shouldRejectMissingChefId()
            throws Exception {
        problemMvc.perform(post("/recipe")
                .content(asJson(UnusedIngredientRequest.builder()
                        .name("BOILED EGGS")
                        .chefId(null)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations[0].field",
                        equalTo("chefId")))
                .andExpect(jsonPath("$.violations[0].message",
                        equalTo("must not be null")))
                .andExpect(jsonPath("$.status",
                        equalTo(UNPROCESSABLE_ENTITY.name())))
        // TODO: Turn off stack traces
        //      .andExpect(jsonPath("$.stackTrace").doesNotExist())
        ;

        verifyNoMoreInteractions(recipes);
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
