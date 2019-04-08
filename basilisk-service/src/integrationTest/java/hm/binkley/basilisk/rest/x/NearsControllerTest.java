package hm.binkley.basilisk.rest.x;

import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.store.x.Near;
import hm.binkley.basilisk.store.x.NearRecord;
import hm.binkley.basilisk.store.x.Nears;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import static hm.binkley.basilisk.rest.x.TestFixtures.fixedNear;
import static hm.binkley.basilisk.rest.x.TestFixtures.fixedNearRecord;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@JsonWebMvcTest(NearsController.class)
class NearsControllerTest {
    private final ResourceLoader loader = new ClassRelativeResourceLoader(
            getClass());

    private final MockMvc controller;

    @MockBean
    private Nears nears;

    private NearRecord nearRecord;
    private Near near;

    @BeforeEach
    void setUp() {
        nearRecord = fixedNearRecord();
        near = fixedNear();

        lenient().doReturn(Optional.of(near))
                .when(nears).byCode(nearRecord.code);
    }

    @Test
    void shouldGetNone()
            throws Exception {
        when(nears.all()).thenReturn(Stream.empty());

        controller.perform(get("/nears/get"))
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "nears-controller-test-get-none.json"), true));
    }

    @Test
    void shouldGetAll()
            throws Exception {
        when(nears.all()).thenReturn(Stream.of(near));

        controller.perform(get("/nears/get"))
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "nears-controller-test-get-all.json"), true));
    }

    @Test
    void shouldGetOne()
            throws Exception {
        when(nears.byCode(nearRecord.code))
                .thenReturn(Optional.of(near));

        controller.perform(get("/nears/get/" + nearRecord.code))
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "nears-controller-test-get-one.json"), true));
    }

    @Disabled("TODO: Implement general 404 handling")
    @Test
    void shouldNotGetOne()
            throws Exception {
        final var code = "ABC";
        when(nears.byCode(code)).thenReturn(Optional.empty());

        controller.perform(get("/nears/get/" + code))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPostOne()
            throws Exception {
        doReturn(near).when(nears).unsaved(nearRecord.code);

        controller.perform(post("/nears/post")
                .content(from("nears-controller-test-post-one-request.json"))
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        LOCATION, "/nears/get/" + nearRecord.code))
                .andExpect(content().json(from(
                        "nears-controller-test-post-one-response.json"),
                        true));
    }

    private String from(final String jsonFile)
            throws IOException {
        // It's a Jedi mind trick
        return new Scanner(
                loader.getResource(jsonFile).readableChannel(), UTF_8)
                .useDelimiter("\\A")
                .next();
    }
}
