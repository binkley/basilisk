package hm.binkley.basilisk.flora.chef.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.chef.Chef;
import hm.binkley.basilisk.flora.chef.Chefs;
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

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_RECIEVED_AT;
import static hm.binkley.basilisk.flora.FloraFixtures.savedChefRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedChefRecord;
import static hm.binkley.basilisk.store.PersistenceTesting.simulateRecordSave;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
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
    private Chefs chefs;

    private static String endpointWithId() { return "/chefs/" + CHEF_ID; }

    private static Map<String, Object> responseMap() {
        return Map.of(
                "id", CHEF_ID,
                "code", CHEF_CODE,
                "name", CHEF_NAME);
    }

    @Test
    void shouldGetAll()
            throws Exception {
        when(chefs.all())
                .thenReturn(Stream.of(new Chef(savedChefRecord())));

        jsonMvc.perform(get("/chefs"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMap()))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        when(chefs.byId(CHEF_ID))
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
        when(chefs.byCode(CHEF_CODE))
                .thenReturn(Optional.of(new Chef(savedChefRecord())));

        jsonMvc.perform(get("/chefs/with-code/" + CHEF_CODE))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMap())));
    }

    @Test
    void shouldNotGetByCode()
            throws Exception {
        jsonMvc.perform(get("/chefs/with-code/" + CHEF_CODE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetByName()
            throws Exception {
        when(chefs.byName(CHEF_NAME))
                .thenReturn(Optional.of(new Chef(savedChefRecord())));

        jsonMvc.perform(get("/chefs/with-name/" + CHEF_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMap())));
    }

    @Test
    void shouldNotGetByName()
            throws Exception {
        jsonMvc.perform(get("/chefs/with-name/" + CHEF_NAME))
                .andExpect(status().isNotFound());
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @Test
    void shouldPostNew()
            throws Exception {
        final ChefRequest request = ChefRequest.builder()
                .code(CHEF_CODE)
                .name(CHEF_NAME)
                .build();

        final var unsaved = spy(unsavedChefRecord());
        when(chefs.unsaved(request.getCode(), request.getName()))
                .thenReturn(new Chef(unsaved));
        doAnswer(simulateRecordSave(CHEF_ID, CHEF_RECIEVED_AT))
                .when(unsaved).save();

        jsonMvc.perform(post("/chefs")
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
