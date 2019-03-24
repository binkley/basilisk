package hm.binkley.basilisk.flora.chef.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.chef.Chef;
import hm.binkley.basilisk.flora.chef.Chefs;
import hm.binkley.basilisk.flora.chef.store.ChefRecord;
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

    private static String endpointWithId() {
        return "/chef/" + CHEF_ID;
    }

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

        jsonMvc.perform(get("/chef"))
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
        when(chefs.byName(CHEF_NAME))
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
        doAnswer(invocation -> {
            final ChefRecord record = (ChefRecord) invocation.getMock();
            record.id = CHEF_ID;
            record.receivedAt = CHEF_RECIEVED_AT;
            return record;
        }).when(unsaved).save();

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
