package hm.binkley.basilisk.flora.source.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.location.Location;
import hm.binkley.basilisk.flora.location.Locations;
import hm.binkley.basilisk.flora.location.rest.LocationRequest;
import hm.binkley.basilisk.flora.source.Source;
import hm.binkley.basilisk.flora.source.Sources;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_RECEIVED_AT;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_RECEIVED_AT;
import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedLocationRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedSourceRecord;
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
                "code", SOURCE_CODE,
                "name", SOURCE_NAME,
                "available-at", List.of());
    }

    private static Map<String, Object> responseMapWithAvailableAt() {
        return Map.of(
                "id", SOURCE_ID,
                "code", SOURCE_CODE,
                "name", SOURCE_NAME,
                "available-at", List.of(Map.of(
                        "id", LOCATION_ID,
                        "code", LOCATION_CODE,
                        "name", LOCATION_NAME)));
    }

    @Test
    void shouldGetAll()
            throws Exception {
        when(sources.all()).thenReturn(
                Stream.of(new Source(savedSourceRecord(), locations)));

        jsonMvc.perform(get("/source"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(List.of(
                        responseMap())), true));
    }

    @Test
    void shouldGetById()
            throws Exception {
        final var record = savedSourceRecord();
        when(sources.byId(record.getId())).thenReturn(
                Optional.of(new Source(record, locations)));

        jsonMvc.perform(get(endpointWithId()))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMap()), true));
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
                .andExpect(content().json(asJson(responseMap()), true));
    }

    @Test
    void shouldNotGetByName()
            throws Exception {
        final var record = savedSourceRecord();
        jsonMvc.perform(get("/source/find/" + record.getName()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPostNewWithNoAvailableAt()
            throws Exception {
        final var unsaved = spy(unsavedSourceRecord());
        final var unsavedLocation = spy(unsavedLocationRecord());
        final SourceRequest request = SourceRequest.builder()
                .code(unsaved.getCode())
                .name(unsaved.getName())
                .availableAt(List.of())
                .build();

        when(sources.unsaved(request.getCode(), request.getName()))
                .thenReturn(new Source(unsaved, locations));
        doAnswer(simulateRecordSave(SOURCE_ID, SOURCE_RECEIVED_AT))
                .when(unsaved).save();
        when(locations.unsaved(
                unsavedLocation.getCode(), unsavedLocation.getName()))
                .thenReturn(new Location(unsavedLocation));
        doAnswer(simulateRecordSave(LOCATION_ID, LOCATION_RECEIVED_AT))
                .when(unsavedLocation).save();

        jsonMvc.perform(post("/source")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(responseMap()), true));
    }

    @Test
    void shouldPostNewWithSomeAvailableAt()
            throws Exception {
        final var unsaved = spy(unsavedSourceRecord());
        final var unsavedLocation = spy(unsavedLocationRecord());
        final SourceRequest request = SourceRequest.builder()
                .code(unsaved.getCode())
                .name(unsaved.getName())
                .availableAt(List.of(LocationRequest.builder()
                        .code(unsavedLocation.getCode())
                        .name(unsavedLocation.getName())
                        .build()))
                .build();

        when(sources.unsaved(request.getCode(), request.getName()))
                .thenReturn(new Source(unsaved, locations));
        doAnswer(simulateRecordSave(SOURCE_ID, SOURCE_RECEIVED_AT))
                .when(unsaved).save();
        final var location = new Location(unsavedLocation);
        when(locations.unsaved(
                unsavedLocation.getCode(), unsavedLocation.getName()))
                .thenReturn(location);
        doAnswer(simulateRecordSave(LOCATION_ID, LOCATION_RECEIVED_AT))
                .when(unsavedLocation).save();
        when(locations.byId(LOCATION_ID))
                .thenReturn(Optional.of(location));

        jsonMvc.perform(post("/source")
                .content(asJson(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, endpointWithId()))
                .andExpect(content().json(asJson(
                        responseMapWithAvailableAt()), true));
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
