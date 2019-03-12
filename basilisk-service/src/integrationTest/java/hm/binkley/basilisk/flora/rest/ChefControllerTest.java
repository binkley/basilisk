package hm.binkley.basilisk.flora.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.domain.Chef;
import hm.binkley.basilisk.flora.domain.Chefs;
import hm.binkley.basilisk.flora.domain.store.ChefRecord;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@JsonWebMvcTest(ChefController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ChefControllerTest {
    private static final Long ID = 17L;

    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Chefs recipes;

    private static String endpointWithId() {
        return "/chef/" + ID;
    }

    private static Map<String, Object> responseMapFor(final String name) {
        return Map.of(
                "id", ID,
                "name", name);
    }

    @Test
    void shouldGetAll()
            throws Exception {
        final String name = "Chef Alice";

        when(recipes.all())
                .thenReturn(Stream.of(new Chef(
                        new ChefRecord(ID, EPOCH, name))));

        jsonMvc.perform(get("/chef"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMapFor(name)))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        final String name = "Chef Bob";

        when(recipes.byId(ID))
                .thenReturn(Optional.of(new Chef(
                        new ChefRecord(ID, EPOCH, name))));

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        responseMapFor(name))));
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
        final String name = "Chef Carol";

        when(recipes.byName(name))
                .thenReturn(Optional.of(new Chef(
                        new ChefRecord(ID, EPOCH, name))));

        jsonMvc.perform(get("/chef/find/" + name))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMapFor(name))));
    }

    @Test
    void shouldPostNew()
            throws Exception {
        final var name = "Chef David";
        final var record = ChefRecord.raw(name);
        final ChefRequest request = ChefRequest.builder()
                .name(name)
                .build();

        when(recipes.create(request))
                .thenReturn(new Chef(new ChefRecord(ID,
                        Instant.ofEpochSecond(1_000_000),
                        record.getName())));

        jsonMvc.perform(post("/chef")
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
