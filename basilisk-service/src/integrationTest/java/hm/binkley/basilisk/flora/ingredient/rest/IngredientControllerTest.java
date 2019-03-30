package hm.binkley.basilisk.flora.ingredient.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.ingredient.Ingredient;
import hm.binkley.basilisk.flora.ingredient.Ingredients;
import hm.binkley.basilisk.flora.ingredient.UnusedIngredient;
import hm.binkley.basilisk.flora.ingredient.UsedIngredient;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.savedUnusedIngredientRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.savedUsedIngredientRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedUnusedIngredientRecord;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfiguration.class)
@JsonWebMvcTest(IngredientController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class IngredientControllerTest {
    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Ingredients ingredients;

    private static String endpointWithId() {
        return "/ingredient/" + INGREDIENT_ID;
    }

    private static Map<String, Object> responseMapForAny(
            final Ingredient ingredient) {
        // As recipeId is nullable, cannot use Map.of()
        final var response = new LinkedHashMap<String, Object>();
        response.put("id", ingredient.getId());
        response.put("code", ingredient.getCode());
        response.put("source-id", ingredient.getSourceId());
        response.put("name", ingredient.getName());
        response.put("quantity", ingredient.getQuantity());
        response.put("recipe-id", ingredient.getRecipeId());
        response.put("chef-id", ingredient.getChefId());
        return response;
    }

    private static Map<String, Object> responseMapForUsed(
            final UsedIngredient ingredient) {
        // As recipeId is nullable, cannot use Map.of()
        final var response = new LinkedHashMap<String, Object>();
        response.put("id", ingredient.getId());
        response.put("code", ingredient.getCode());
        response.put("source-id", ingredient.getSourceId());
        response.put("name", ingredient.getName());
        response.put("quantity", ingredient.getQuantity());
        response.put("recipe-id", ingredient.getRecipeId());
        response.put("chef-id", ingredient.getChefId());
        return response;
    }

    private static Map<String, Object> responseMapForUnused(
            final UnusedIngredient ingredient) {
        // As recipeId is nullable, cannot use Map.of()
        final var response = new LinkedHashMap<String, Object>();
        response.put("id", ingredient.getId());
        response.put("code", ingredient.getCode());
        response.put("source-id", ingredient.getSourceId());
        response.put("name", ingredient.getName());
        response.put("quantity", ingredient.getQuantity());
        response.put("chef-id", ingredient.getChefId());
        return response;
    }

    @Test
    void shouldGetAll()
            throws Exception {
        final var record = savedUnusedIngredientRecord();
        final var unusedIngredient = new UnusedIngredient(record, null);
        final var usedIngredient = new UsedIngredient(
                new IngredientRecord(record.getId() + 1,
                        record.getReceivedAt(), record.getCode() + "x",
                        record.getSourceId() + 1, record.getName() + "x",
                        record.getQuantity(), record.getRecipeId(),
                        record.getChefId()), null);

        when(ingredients.all())
                .thenReturn(Stream.of(unusedIngredient, usedIngredient));

        jsonMvc.perform(get("/ingredient"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(List.of(
                        responseMapForAny(unusedIngredient),
                        responseMapForAny(usedIngredient))), true));
    }

    @Test
    void shouldGetUnused()
            throws Exception {
        final var ingredient = new UnusedIngredient(
                savedUnusedIngredientRecord(), null);
        when(ingredients.allUnused())
                .thenReturn(Stream.of(ingredient));

        jsonMvc.perform(get("/ingredient/unused"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(List.of(
                        responseMapForUnused(ingredient))), true));
    }

    @Test
    void shouldGetById()
            throws Exception {
        final var ingredient = new UnusedIngredient(
                savedUnusedIngredientRecord(), null);
        when(ingredients.byId(ingredient.getId()))
                .thenReturn(Optional.of(ingredient));

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        responseMapForAny(ingredient)), true));
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
        final var ingredient = new UsedIngredient(
                savedUsedIngredientRecord(), null);
        when(ingredients.allByName(ingredient.getName()))
                .thenReturn(Stream.of(ingredient));

        jsonMvc.perform(get("/ingredient/find/" + ingredient.getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(List.of(
                        responseMapForUsed(ingredient))), true));
    }

    @Test
    void shouldPostNew()
            throws Exception {
        final var record = unsavedUnusedIngredientRecord();
        final UnusedIngredientRequest request = UnusedIngredientRequest
                .builder()
                .code(record.getCode())
                .sourceId(record.getSourceId())
                .name(record.getName())
                .quantity(record.getQuantity())
                .chefId(record.getChefId())
                .build();

        final var ingredient = new UnusedIngredient(
                savedUnusedIngredientRecord(), null);
        when(ingredients.createUnused(request))
                .thenReturn(ingredient);

        jsonMvc.perform(post("/ingredient")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(
                        responseMapForUnused(ingredient)), true));
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
