package hm.binkley.basilisk.flora.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.domain.Ingredients;
import hm.binkley.basilisk.flora.domain.UnusedIngredient;
import hm.binkley.basilisk.flora.domain.UsedIngredient;
import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_NAME;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedUnusedIngredientRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedUsedIngredientRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedIngredientRecord;
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
                "name", INGREDIENT_NAME,
                "chef-id", CHEF_ID);
    }

    private static Map<String, Object> responseMapFor(
            final Long id, final String name, final Long recipeId) {
        // As recipeId is nullable, cannot use Map.of()
        final var response = new LinkedHashMap<String, Object>();
        response.put("id", id);
        response.put("name", name);
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
                        responseMapFor(
                                unusedIngredient.getId(),
                                unusedIngredient.getName(),
                                null),
                        responseMapFor(
                                usedIngredient.getId(),
                                usedIngredient.getName(),
                                usedIngredient.getRecipeId())))));
    }

    @Test
    void shouldGetUnused()
            throws Exception {
        when(ingredients.unused())
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
                        new IngredientRecord(
                                INGREDIENT_ID, EPOCH, INGREDIENT_NAME, ONE,
                                null, CHEF_ID))));

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMap(
                ))));
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
        when(ingredients.byName(INGREDIENT_NAME))
                .thenReturn(Stream.of(new UsedIngredient(
                        savedUsedIngredientRecord())));

        jsonMvc.perform(get("/ingredient/find/" + INGREDIENT_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        Set.of(responseMap()))));
    }

    @Test
    void shouldPostNew()
            throws Exception {
        final var record = unsavedIngredientRecord();
        final UnusedIngredientRequest request = UnusedIngredientRequest
                .builder()
                .name(record.getName())
                .quantity(record.getQuantity())
                .chefId(record.getChefId())
                .build();

        when(ingredients.createUnused(request))
                .thenReturn(new UnusedIngredient(new IngredientRecord(
                        INGREDIENT_ID, Instant.ofEpochSecond(1_000_000),
                        record.getName(), record.getQuantity(), null,
                        record.getChefId())));

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
