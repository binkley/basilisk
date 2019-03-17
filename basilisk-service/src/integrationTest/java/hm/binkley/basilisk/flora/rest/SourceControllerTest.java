package hm.binkley.basilisk.flora.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.flora.domain.Source;
import hm.binkley.basilisk.flora.domain.Sources;
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

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedSourceRecord;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JsonConfiguration.class,
        WorkaroundComponentScanFindingAllConverters.class})
@JsonWebMvcTest(SourceController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SourceControllerTest {
    private final MockMvc jsonMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Sources sources;

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
        when(sources.all())
                .thenReturn(Stream.of(new Source(savedSourceRecord())));

        jsonMvc.perform(get("/source"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(Set.of(responseMap()))));
    }

    @Test
    void shouldGetById()
            throws Exception {
        when(sources.byId(SOURCE_ID))
                .thenReturn(Optional.of(new Source(savedSourceRecord())));

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
        when(sources.byName(SOURCE_NAME))
                .thenReturn(Optional.of(new Source(savedSourceRecord())));

        jsonMvc.perform(get("/source/find/" + SOURCE_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(responseMap())));
    }

    @Test
    void shouldNotGetByName()
            throws Exception {
        jsonMvc.perform(get("/source/find/" + SOURCE_NAME))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPostNew()
            throws Exception {
        final var unsaved = unsavedSourceRecord();
        final SourceRequest request = SourceRequest
                .builder()
                .name(unsaved.getName())
                .build();

        when(sources.create(request))
                .thenReturn(new Source(savedSourceRecord()));

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
