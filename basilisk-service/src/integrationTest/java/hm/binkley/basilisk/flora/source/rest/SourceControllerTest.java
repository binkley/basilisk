package hm.binkley.basilisk.flora.source.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.location.Locations;
import hm.binkley.basilisk.flora.source.Source;
import hm.binkley.basilisk.flora.source.Sources;
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

import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedSourceRecord;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfiguration.class)
@JsonWebMvcTest(SourceController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SourceControllerTest {
    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Sources sources;
    @MockBean
    private Locations locations;

    private static String endpointWithId() {
        return "/source/" + SOURCE_ID;
    }

    private static Map<String, Object> responseMap() {
        return Map.of(
                "id", SOURCE_ID,
                "name", SOURCE_NAME);
    }

    @Test
    void shouldGetAll()
            throws Exception {
        when(sources.all()).thenReturn(
                Stream.of(new Source(savedSourceRecord(), locations)));

        jsonMvc.perform(get("/source"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(responseMap()))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        final var record = savedSourceRecord();
        when(sources.byId(record.getId())).thenReturn(
                Optional.of(new Source(record, locations)));

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
        final var record = savedSourceRecord();
        when(sources.byName(record.getName())).thenReturn(
                Optional.of(new Source(record, locations)));

        jsonMvc.perform(get("/source/find/" + record.getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMap())));
    }

    @Test
    void shouldNotGetByName()
            throws Exception {
        final var record = savedSourceRecord();
        jsonMvc.perform(get("/source/find/" + record.getName()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPostNew()
            throws Exception {
        final var unsaved = unsavedSourceRecord();
        final SourceRequest request = SourceRequest.builder()
                .name(unsaved.getName())
                .build();

        when(sources.create(request)).thenReturn(
                new Source(savedSourceRecord(), locations));

        jsonMvc.perform(post("/source")
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
