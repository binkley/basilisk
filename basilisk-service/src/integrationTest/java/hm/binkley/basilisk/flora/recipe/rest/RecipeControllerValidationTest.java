package hm.binkley.basilisk.flora.recipe.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.LoggingConfiguration;
import hm.binkley.basilisk.configuration.ProblemWebMvcTest;
import hm.binkley.basilisk.flora.recipe.Recipes;
import hm.binkley.basilisk.flora.service.SpecialService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.RECIPE_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.RECIPE_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JsonConfiguration.class, LoggingConfiguration.class})
@ProblemWebMvcTest(RecipeController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class RecipeControllerValidationTest {
    private static final String ROOT = "/recipes";
    private static final String FIRST_FIELD = "$.violations[0].field";
    private static final String FIRST_MESSAGE = "$.violations[0].message";
    private static final String STATUS = "$.status";
    private static final String STACK_TRACE = "$.stack-trace";

    private final MockMvc problemMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Recipes recipes;
    /** @todo Required for injection, but unused in this test */
    @MockBean
    private SpecialService specialService;

    @SuppressFBWarnings("RV")
    @Test
    void shouldRejectShortNames()
            throws Exception {
        problemMvc.perform(get("/recipes/find/F"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(FIRST_FIELD,
                        equalTo("getByName.name")))
                .andExpect(jsonPath(FIRST_MESSAGE,
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath(STATUS,
                        equalTo(UNPROCESSABLE_ENTITY.value())))
                .andExpect(jsonPath(STACK_TRACE).doesNotExist());

        verifyNoMoreInteractions(recipes);
    }

    @Test
    void shouldRejectShortRequestCodes()
            throws Exception {
        problemMvc.perform(post(ROOT)
                .content(asJson(RecipeRequest.builder()
                        .code(RECIPE_CODE.substring(0, 1))
                        .name(RECIPE_NAME)
                        .chefId(CHEF_ID)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(FIRST_FIELD,
                        equalTo("code")))
                .andExpect(jsonPath(FIRST_MESSAGE,
                        equalTo("length must be between 3 and 8")))
                .andExpect(jsonPath(STATUS,
                        equalTo(UNPROCESSABLE_ENTITY.value())))
                .andExpect(jsonPath(STACK_TRACE).doesNotExist());

        verifyNoMoreInteractions(recipes);
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    @Test
    void shouldRejectShortRequestNames()
            throws Exception {
        problemMvc.perform(post(ROOT)
                .content(asJson(RecipeRequest.builder()
                        .code(RECIPE_CODE)
                        .name(RECIPE_NAME.substring(0, 1))
                        .chefId(CHEF_ID)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(FIRST_FIELD,
                        equalTo("name")))
                .andExpect(jsonPath(FIRST_MESSAGE,
                        equalTo("length must be between 3 and 32")))
                .andExpect(jsonPath(STATUS,
                        equalTo(UNPROCESSABLE_ENTITY.value())))
                .andExpect(jsonPath(STACK_TRACE).doesNotExist());

        verifyNoMoreInteractions(recipes);
    }

    @Test
    void shouldRejectMissingChefId()
            throws Exception {
        problemMvc.perform(post(ROOT)
                .content(asJson(RecipeRequest.builder()
                        .code(RECIPE_CODE)
                        .name(RECIPE_NAME)
                        .chefId(null)
                        .build())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath(FIRST_FIELD,
                        equalTo("chefId")))
                .andExpect(jsonPath(FIRST_MESSAGE,
                        equalTo("must not be null")))
                .andExpect(jsonPath(STATUS,
                        equalTo(UNPROCESSABLE_ENTITY.value())))
                .andExpect(jsonPath(STACK_TRACE).doesNotExist());

        verifyNoMoreInteractions(recipes);
    }
}
