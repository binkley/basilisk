package hm.binkley.basilisk.flora.location.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.location.Location;
import hm.binkley.basilisk.flora.location.Locations;
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

import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.savedLocationRecord;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfiguration.class)
@JsonWebMvcTest(LocationController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class LocationControllerTest {
    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Locations recipes;

    private static String endpointWithId() {
        return "/location/" + LOCATION_ID;
    }

    private static Map<String, Object> responseMap() {
        return Map.of(
                "id", LOCATION_ID,
                "name", LOCATION_NAME);
    }

    @Test
    void shouldGetAll()
            throws Exception {
        when(recipes.all())
                .thenReturn(Stream.of(new Location(savedLocationRecord())));

        jsonMvc.perform(get("/location"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(
                        responseMap()))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        when(recipes.byId(LOCATION_ID))
                .thenReturn(Optional.of(new Location(savedLocationRecord())));

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
    void shouldGetByName()
            throws Exception {
        when(recipes.byName(LOCATION_NAME))
                .thenReturn(Optional.of(new Location(savedLocationRecord())));

        jsonMvc.perform(get("/location/with-name/" + LOCATION_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMap())));
    }

    @Test
    void shouldNotGetByName()
            throws Exception {
        jsonMvc.perform(get("/location/with-name/" + LOCATION_NAME))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPostNew()
            throws Exception {
        final LocationRequest request = LocationRequest.builder()
                .name(LOCATION_NAME)
                .build();

        when(recipes.create(request))
                .thenReturn(new Location(savedLocationRecord()));

        jsonMvc.perform(post("/location")
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
