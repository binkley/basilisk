package hm.binkley.basilisk.flora.recipe.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.ingredient.rest.UsedIngredientRequest;
import hm.binkley.basilisk.flora.recipe.Recipe;
import hm.binkley.basilisk.flora.recipe.Recipes;
import hm.binkley.basilisk.flora.service.SpecialService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_QUANTITY;
import static hm.binkley.basilisk.flora.FloraFixtures.RECIPE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.savedRecipeRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.savedUsedIngredientRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedRecipeRecord;
import static hm.binkley.basilisk.flora.recipe.rest.RecipeController.CREATED_RECIPE;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfiguration.class)
@JsonWebMvcTest(RecipeController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class RecipeControllerTest {
    // Make PMD happy
    private static final String ID_WORD = "id";
    private static final String NAME_WORD = "name";
    private static final String QUANTITY_WORD = "quantity";
    private static final String CHEF_ID_WORD = "chef-id";
    private static final String DAILY_SPECIAL_WORD = "daily-special";
    private static final String INGREDIENTS_WORD = "ingredients";
    private static final String RECIPE_ID_WORD = "recipe-id";

    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;
    private final RecipeController controller;

    @MockBean
    private Recipes recipes;
    @MockBean
    private SpecialService specialService;
    @MockBean
    private Logger logger;

    private static String endpointWithId() {
        return "/recipe/" + RECIPE_ID;
    }

    private static Map<String, Object> responseMapFor(final String name,
            final boolean hasIngredients) {
        return hasIngredients
                ? Map.of(
                ID_WORD, RECIPE_ID,
                NAME_WORD, name,
                DAILY_SPECIAL_WORD, true,
                CHEF_ID_WORD, CHEF_ID,
                INGREDIENTS_WORD, List.of(Map.of(
                        ID_WORD, INGREDIENT_ID,
                        NAME_WORD, SOURCE_NAME,
                        QUANTITY_WORD, INGREDIENT_QUANTITY,
                        CHEF_ID_WORD, CHEF_ID,
                        RECIPE_ID_WORD, RECIPE_ID)))
                : Map.of(
                        ID_WORD, RECIPE_ID,
                        NAME_WORD, name,
                        DAILY_SPECIAL_WORD, false,
                        CHEF_ID_WORD, CHEF_ID,
                        INGREDIENTS_WORD, List.of());
    }

    @Test
    void shouldGetAll()
            throws Exception {
        final var record = savedRecipeRecord();
        final var recipe = new Recipe(record);
        when(recipes.all())
                .thenReturn(Stream.of(recipe));
        when(specialService.isDailySpecial(recipe))
                .thenReturn(false);

        jsonMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMapFor(recipe.getName(), false)))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        final var record = savedRecipeRecord();
        final var recipe = new Recipe(record);
        when(recipes.byId(record.getId()))
                .thenReturn(Optional.of(recipe));
        when(specialService.isDailySpecial(recipe))
                .thenReturn(false);

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        responseMapFor(recipe.getName(), false))));
    }

    @Test
    void shouldNotGetById()
            throws Exception {
        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetByName()
            throws Exception {
        final var record = savedRecipeRecord();
        when(recipes.byName(record.getName()))
                .thenReturn(Optional.of(new Recipe(record)));

        jsonMvc.perform(get("/recipe/find/" + record.getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        responseMapFor(record.getName(), false))));
    }

    @Test
    void shouldPostNewWithNoIngredients()
            throws Exception {
        final var record = unsavedRecipeRecord();
        final RecipeRequest request = RecipeRequest.builder()
                .name(record.getName())
                .chefId(record.getChefId())
                .build();

        final var recipe = new Recipe(savedRecipeRecord());
        when(recipes.create(request))
                .thenReturn(recipe);

        jsonMvc.perform(post("/recipe")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(
                        responseMapFor(record.getName(), false))));

        final var response = controller.toResponse().apply(recipe);
        verify(logger).info(CREATED_RECIPE, response);
    }

    @Test
    void shouldPostNewWithSomeIngredients()
            throws Exception {
        final var record = unsavedRecipeRecord();
        final RecipeRequest request = RecipeRequest.builder()
                .name(record.getName())
                .chefId(record.getChefId())
                .ingredients(Set.of(UsedIngredientRequest.builder()
                        .name(SOURCE_NAME)
                        .quantity(INGREDIENT_QUANTITY)
                        .chefId(CHEF_ID)
                        .build()))
                .build();

        final var recipe = new Recipe(savedRecipeRecord()
                .add(savedUsedIngredientRecord()));
        when(specialService.isDailySpecial(recipe))
                .thenReturn(true);
        when(recipes.create(request))
                .thenReturn(recipe);

        jsonMvc.perform(post("/recipe")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(
                        responseMapFor(record.getName(), true))));
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
