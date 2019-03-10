package hm.binkley.basilisk.flora.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.domain.Recipe;
import hm.binkley.basilisk.flora.domain.Recipes;
import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
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
@JsonWebMvcTest(RecipeController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class RecipeControllerTest {
    private static final long ID = 1L;

    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;
    private final Recipes recipes;

    private static String endpointWithId() {
        return "/recipe/" + ID;
    }

    private static Map<String, Object> responseMapFor(final String name) {
        return Map.of(
                "id", ID,
                "name", name);
    }

    @Test
    void shouldFindAll()
            throws Exception {
        final String name = "POACHED EGGS";

        when(recipes.all())
                .thenReturn(Stream.of(new Recipe(
                        new RecipeRecord(ID, EPOCH, name))));

        jsonMvc.perform(get("/recipe"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMapFor(name)))));
    }

    @Test
    void shouldFindExplicitly()
            throws Exception {
        final String name = "FRIED EGGS";

        when(recipes.byId(ID))
                .thenReturn(Optional.of(new Recipe(
                        new RecipeRecord(ID, EPOCH, name))));

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        responseMapFor(name))));
    }

    @Test
    void shouldNotFindExplicitly()
            throws Exception {
        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindByName()
            throws Exception {
        final String name = "BOILED EGGS";

        when(recipes.byName(name))
                .thenReturn(Stream.of(new Recipe(
                        new RecipeRecord(ID, EPOCH, name))));

        jsonMvc.perform(get("/recipe/find/" + name))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMapFor(name)))));
    }

    @Test
    void shouldCreateNew()
            throws Exception {
        final var name = "SOUFFLE";
        final var record = RecipeRecord.raw(name);
        final RecipeRequest request = RecipeRequest.builder()
                .name(name)
                .build();

        when(recipes.create(request))
                .thenReturn(new Recipe(new RecipeRecord(ID,
                        Instant.ofEpochSecond(1_000_000),
                        record.getName())));

        jsonMvc.perform(post("/recipe")
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
