package hm.binkley.basilisk.flora.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.domain.Recipe;
import hm.binkley.basilisk.flora.domain.Recipes;
import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import hm.binkley.basilisk.flora.service.SpecialService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_QUANTITY;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.RECIPE_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedUsedIngredientRecord;
import static java.time.Instant.EPOCH;
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

    @MockBean
    private Recipes recipes;
    @MockBean
    private SpecialService specialService;

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
        final String name = "POACHED EGGS";

        final var recipe = new Recipe(
                new RecipeRecord(RECIPE_ID, EPOCH, name, CHEF_ID));
        when(recipes.all())
                .thenReturn(Stream.of(recipe));
        when(specialService.isDailySpecial(recipe))
                .thenReturn(false);

        jsonMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMapFor(name, false)))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        final String name = "FRIED EGGS";

        final var recipe = new Recipe(
                new RecipeRecord(RECIPE_ID, EPOCH, name, CHEF_ID));
        when(recipes.byId(RECIPE_ID))
                .thenReturn(Optional.of(recipe));
        when(specialService.isDailySpecial(recipe))
                .thenReturn(false);

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        responseMapFor(name, false))));
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
        final String name = "BOILED EGGS";

        when(recipes.byName(name))
                .thenReturn(Optional.of(new Recipe(
                        new RecipeRecord(RECIPE_ID, EPOCH, name, CHEF_ID))));

        jsonMvc.perform(get("/recipe/find/" + name))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        responseMapFor(name, false))));
    }

    @Test
    void shouldPostNewWithNoIngredients()
            throws Exception {
        final var name = "SOUFFLE";
        final var record = RecipeRecord.raw(name, CHEF_ID);
        final RecipeRequest request = RecipeRequest.builder()
                .name(name)
                .chefId(CHEF_ID)
                .build();

        when(recipes.create(request))
                .thenReturn(new Recipe(new RecipeRecord(RECIPE_ID,
                        Instant.ofEpochSecond(1_000_000),
                        record.getName(), record.getChefId())));

        jsonMvc.perform(post("/recipe")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(
                        responseMapFor(name, false))));
    }

    @Test
    void shouldPostNewWithSomeIngredients()
            throws Exception {
        final var name = "SOUFFLE";
        final var record = RecipeRecord.raw(name, CHEF_ID);
        final RecipeRequest request = RecipeRequest.builder()
                .name(name)
                .chefId(CHEF_ID)
                .ingredients(Set.of(UsedIngredientRequest.builder()
                        .name(SOURCE_NAME)
                        .quantity(INGREDIENT_QUANTITY)
                        .chefId(CHEF_ID)
                        .build()))
                .build();

        final var recipe = new Recipe(new RecipeRecord(RECIPE_ID,
                EPOCH.plusSeconds(1_000_000), record.getName(),
                record.getChefId())
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
                        responseMapFor(name, true))));
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
