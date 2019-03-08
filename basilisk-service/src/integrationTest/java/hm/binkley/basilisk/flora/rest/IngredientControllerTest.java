package hm.binkley.basilisk.flora.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.basilisk.domain.Basilisks;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.domain.Ingredient;
import hm.binkley.basilisk.flora.domain.Ingredients;
import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
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
import java.util.stream.Stream;

import static java.time.Instant.EPOCH;
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
    private static final long ID = 1L;
    private static final long PARENT_ID = 2L;

    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Ingredients ingredients;

    @MockBean
    private Basilisks thisIsSad;

    private static String endpointWithId() {
        return "/ingredient/" + ID;
    }

    private static Map<String, Object> responseMapFor(final String name) {
        return Map.of(
                "id", ID,
                "name", name);
    }

    @Test
    void shouldFindAll()
            throws Exception {
        final String name = "EGGS";

        when(ingredients.all())
                .thenReturn(Stream.of(new Ingredient(
                        new IngredientRecord(ID, EPOCH, name, PARENT_ID))));

        jsonMvc.perform(get("/ingredient"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(List.of(
                        responseMapFor(name)))));
    }

    @Test
    void shouldFindExplicitly()
            throws Exception {
        final String name = "MILK";

        when(ingredients.byId(ID))
                .thenReturn(Optional.of(new Ingredient(
                        new IngredientRecord(ID, EPOCH, name, PARENT_ID))));

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
    void shouldAcceptWords()
            throws Exception {
        final String name = "BACON";

        when(ingredients.byName(name))
                .thenReturn(Stream.of(new Ingredient(
                        new IngredientRecord(ID, EPOCH, name, PARENT_ID))));

        jsonMvc.perform(get("/ingredient/find/" + name))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(List.of(
                        responseMapFor(name)))));
    }

    @Test
    void shouldAddRecordWithNoCockatrices()
            throws Exception {
        final var name = "SALT";
        final var record = IngredientRecord.raw(name);
        final IngredientRequest request = IngredientRequest.builder()
                .name(name)
                .build();

        when(ingredients.create(request))
                .thenReturn(new Ingredient(new IngredientRecord(ID,
                        Instant.ofEpochSecond(1_000_000),
                        record.getName(), PARENT_ID)));

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
