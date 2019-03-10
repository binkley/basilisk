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
    private static final long ID = 1L;

    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Ingredients ingredients;

    private static String endpointWithId() {
        return "/ingredient/" + ID;
    }

    private static Map<String, Object> responseMapFor(final String name) {
        return Map.of(
                "id", ID,
                "name", name);
    }

    private static Map<String, Object> responseMapFor(
            final Long id, final String name, final Long recipeId) {
        // As recipeId is nullable, cannot use Map.of()
        final var response = new LinkedHashMap<String, Object>();
        response.put("id", id);
        response.put("name", name);
        response.put("recipe-id", recipeId);
        return response;
    }

    @Test
    void shouldGetAll()
            throws Exception {
        final var unusedIngredient = new UnusedIngredient(
                new IngredientRecord(ID, EPOCH, "EGGS", null));
        final var usedIngredient = new UsedIngredient(new IngredientRecord(
                ID + 1, EPOCH.plusSeconds(1L), "BACON", 2L));

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
        final var name = "EGGS";

        when(ingredients.unused())
                .thenReturn(Stream.of(new UnusedIngredient(
                        new IngredientRecord(ID, EPOCH, name, null))));

        jsonMvc.perform(get("/ingredient/unused"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMapFor(name)))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        final var name = "MILK";

        when(ingredients.byId(ID))
                .thenReturn(Optional.of(new UnusedIngredient(
                        new IngredientRecord(ID, EPOCH, name, null))));

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMapFor(name))));
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
        final var name = "BACON";

        when(ingredients.byName(name))
                .thenReturn(Optional.of(new UsedIngredient(
                        new IngredientRecord(ID, EPOCH, name, 2L))));

        jsonMvc.perform(get("/ingredient/find/" + name))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMapFor(name))));
    }

    @Test
    void shouldCreateNew()
            throws Exception {
        final var name = "SALT";
        final var record = IngredientRecord.raw(name);
        final UnusedIngredientRequest request = UnusedIngredientRequest
                .builder()
                .name(name)
                .build();

        when(ingredients.createUnused(request))
                .thenReturn(new UnusedIngredient(new IngredientRecord(
                        ID, Instant.ofEpochSecond(1_000_000),
                        record.getName(), null)));

        jsonMvc.perform(post("/ingredient")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(
                        responseMapFor(name))));
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
