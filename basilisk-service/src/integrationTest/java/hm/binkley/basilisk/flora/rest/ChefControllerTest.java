package hm.binkley.basilisk.flora.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.domain.Chef;
import hm.binkley.basilisk.flora.domain.Chefs;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_CODE;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_NAME;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedChefRecord;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfiguration.class)
@JsonWebMvcTest(ChefController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ChefControllerTest {
    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Chefs recipes;

    private static String endpointWithId() {
        return "/chef/" + CHEF_ID;
    }

    private static Map<String, Object> responseMap() {
        return Map.of(
                "id", CHEF_ID,
                "name", CHEF_NAME);
    }

    @Test
    void shouldGetAll()
            throws Exception {
        when(recipes.all())
                .thenReturn(Stream.of(new Chef(savedChefRecord())));

        jsonMvc.perform(get("/chef"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMap()))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        when(recipes.byId(CHEF_ID))
                .thenReturn(Optional.of(new Chef(savedChefRecord())));

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(
                        responseMap())));
    }

    @Test
    void shouldNotGetById()
            throws Exception {
        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetByCode()
            throws Exception {
        when(recipes.byCode(CHEF_CODE))
                .thenReturn(Optional.of(new Chef(savedChefRecord())));

        jsonMvc.perform(get("/chef/with-code/" + CHEF_CODE))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMap())));
    }

    @Test
    void shouldNotGetByCode()
            throws Exception {
        jsonMvc.perform(get("/chef/with-code/" + CHEF_CODE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetByName()
            throws Exception {
        when(recipes.byName(CHEF_NAME))
                .thenReturn(Optional.of(new Chef(savedChefRecord())));

        jsonMvc.perform(get("/chef/with-name/" + CHEF_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMap())));
    }

    @Test
    void shouldNotGetByName()
            throws Exception {
        jsonMvc.perform(get("/chef/with-name/" + CHEF_NAME))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPostNew()
            throws Exception {
        final ChefRequest request = ChefRequest.builder()
                .name(CHEF_NAME)
                .build();

        when(recipes.create(request))
                .thenReturn(new Chef(savedChefRecord()));

        jsonMvc.perform(post("/chef")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(responseMap())));
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
