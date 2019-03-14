package hm.binkley.basilisk.flora.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.domain.Recipe;
import hm.binkley.basilisk.flora.domain.Recipes;
import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import hm.binkley.basilisk.flora.service.SpecialService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.math.BigDecimal.ONE;
import static java.time.Instant.EPOCH;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JsonConfiguration.class,
        WorkaroundComponentScanFindingAllConverters.class})
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

    private static final Long ID = 1L;
    private static final Long INGREDIENT_ID = 2L;
    private static final Long CHEF_ID = 17L;
    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Recipes recipes;
    @MockBean
    private SpecialService specialService;

    private static String endpointWithId() {
        return "/recipe/" + ID;
    }

    private static Map<String, Object> responseMapFor(
            final String name, final boolean dailySpecial) {
        return Map.of(
                ID_WORD, ID,
                NAME_WORD, name,
                DAILY_SPECIAL_WORD, dailySpecial,
                CHEF_ID_WORD, CHEF_ID);
    }

    private static Map<String, Object> responseMapFor(final String name,
            final String ingredientName,
            final BigDecimal ingredientQuantity) {
        if (null == ingredientName) return Map.of(
                ID_WORD, ID,
                NAME_WORD, name,
                DAILY_SPECIAL_WORD, false,
                CHEF_ID_WORD, CHEF_ID,
                INGREDIENTS_WORD, List.of());
        return Map.of(
                ID_WORD, ID,
                NAME_WORD, name,
                CHEF_ID_WORD, CHEF_ID,
                INGREDIENTS_WORD, List.of(Map.of(
                        ID_WORD, INGREDIENT_ID,
                        NAME_WORD, ingredientName,
                        QUANTITY_WORD, ingredientQuantity,
                        CHEF_ID_WORD, CHEF_ID,
                        RECIPE_ID_WORD, ID)));
    }

    @Test
    void shouldGetAll()
            throws Exception {
        final String name = "POACHED EGGS";

        final var recipe = new Recipe(
                new RecipeRecord(ID, EPOCH, name, CHEF_ID));
        when(recipes.all())
                .thenReturn(Stream.of(recipe));
        when(specialService.isDailySpecial(recipe))
                .thenReturn(true);

        jsonMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMapFor(name, true)))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        final String name = "FRIED EGGS";

        final var recipe = new Recipe(
                new RecipeRecord(ID, EPOCH, name, CHEF_ID));
        when(recipes.byId(ID))
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
                        new RecipeRecord(ID, EPOCH, name, CHEF_ID))));

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
                .thenReturn(new Recipe(new RecipeRecord(ID,
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
        final var ingredientName = "EGGS";
        final var ingredientQuantity = ONE;
        final var record = RecipeRecord.raw(name, CHEF_ID);
        final var ingredientRecord = IngredientRecord.raw(
                ingredientName, ingredientQuantity, CHEF_ID);
        final RecipeRequest request = RecipeRequest.builder()
                .name(name)
                .chefId(CHEF_ID)
                .ingredients(Set.of(UsedIngredientRequest.builder()
                        .name(ingredientName)
                        .quantity(ingredientQuantity)
                        .chefId(CHEF_ID)
                        .build()))
                .build();

        when(recipes.create(request))
                .thenReturn(new Recipe(new RecipeRecord(ID,
                        EPOCH.plusSeconds(1_000_000),
                        record.getName(), record.getChefId())
                        .add(new IngredientRecord(2L,
                                EPOCH.plusSeconds(1_000_001),
                                ingredientRecord.getName(),
                                ingredientRecord.getQuantity(),
                                ID, CHEF_ID))));

        jsonMvc.perform(post("/recipe")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(
                        responseMapFor(name, ingredientName,
                                ingredientQuantity))));
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
