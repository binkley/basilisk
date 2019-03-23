package hm.binkley.basilisk.flora.ingredient.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.ingredient.Ingredients;
import hm.binkley.basilisk.flora.ingredient.UnusedIngredient;
import hm.binkley.basilisk.flora.ingredient.UsedIngredient;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
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

    private static Map<String, Object> responseMap() {
        return Map.of(
                "id", INGREDIENT_ID,
                "source-id", SOURCE_ID,
                "name", SOURCE_NAME,
                "chef-id", CHEF_ID);
    }

    private static Map<String, Object> responseMapFor(
            final Long recipeId) {
        // As recipeId is nullable, cannot use Map.of()
        final var response = new LinkedHashMap<String, Object>();
        response.put("id", INGREDIENT_ID);
        response.put("source-id", SOURCE_ID);
        response.put("name", SOURCE_NAME);
        response.put("recipe-id", recipeId);
        response.put("chef-id", CHEF_ID);
        return response;
    }

    @Test
    void shouldGetAll()
            throws Exception {
        final var unusedIngredient = new UnusedIngredient(
                savedUnusedIngredientRecord());
        final var usedIngredient = new UsedIngredient(
                savedUsedIngredientRecord());

        when(ingredients.all())
                .thenReturn(Stream.of(unusedIngredient, usedIngredient));

        jsonMvc.perform(get("/ingredient"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMapFor(null),
                        responseMapFor(usedIngredient.getRecipeId())))));
    }

    @Test
    void shouldGetUnused()
            throws Exception {
        when(ingredients.allUnused())
                .thenReturn(Stream.of(new UnusedIngredient(
                        savedUnusedIngredientRecord())));

        jsonMvc.perform(get("/ingredient/unused"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMap()))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        when(ingredients.byId(INGREDIENT_ID))
                .thenReturn(Optional.of(new UnusedIngredient(
                        savedUnusedIngredientRecord())));

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMap())));
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
        when(ingredients.allByName(SOURCE_NAME))
                .thenReturn(Stream.of(new UsedIngredient(
                        savedUsedIngredientRecord())));

        jsonMvc.perform(get("/ingredient/find/" + SOURCE_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        Set.of(responseMap()))));
    }

    @Test
    void shouldPostNew()
            throws Exception {
        final var record = unsavedUnusedIngredientRecord();
        final UnusedIngredientRequest request = UnusedIngredientRequest
                .builder()
                .sourceId(record.getSourceId())
                .name(record.getName())
                .quantity(record.getQuantity())
                .chefId(record.getChefId())
                .build();

        when(ingredients.createUnused(request))
                .thenReturn(new UnusedIngredient(
                        savedUnusedIngredientRecord()));

        jsonMvc.perform(post("/ingredient")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(
                        responseMap())));
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
