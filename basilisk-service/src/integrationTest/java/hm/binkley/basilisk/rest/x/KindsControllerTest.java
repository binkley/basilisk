package hm.binkley.basilisk.rest.x;

import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.store.x.Kind;
import hm.binkley.basilisk.store.x.KindRecord;
import hm.binkley.basilisk.store.x.Kinds;
import hm.binkley.basilisk.store.x.Near;
import hm.binkley.basilisk.store.x.Nears;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import static hm.binkley.basilisk.rest.x.TestFixtures.fixedKind;
import static hm.binkley.basilisk.rest.x.TestFixtures.fixedKindRecord;
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
@JsonWebMvcTest(KindsController.class)
class KindsControllerTest {
    private final MockMvc controller;

    @MockBean
    private Kinds kinds;
    @MockBean
    private Nears nears;

    private Near near;
    private KindRecord kindRecord;
    private Kind kind;

    @BeforeEach
    void setUp() {
        final var nearRecord = fixedNearRecord();
        near = fixedNear();
        kindRecord = fixedKindRecord();
        kind = fixedKind(nears);

        lenient().doReturn(Optional.of(near))
                .when(nears).byCode(nearRecord.code);
        lenient().doReturn(Optional.of(kind))
                .when(kinds).byCode(kindRecord.code);
    }

    @Test
    void shouldGetNone()
            throws Exception {
        when(kinds.all()).thenReturn(Stream.empty());

        controller.perform(get("/kinds/get"))
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "kinds-controller-test-get-none-response.json"),
                        true));
    }

    @Test
    void shouldGetAll()
            throws Exception {
        kind.addNear(near);
        when(kinds.all()).thenReturn(Stream.of(kind));

        controller.perform(get("/kinds/get"))
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "kinds-controller-test-get-all-response.json"),
                        true));
    }

    @Test
    void shouldGetOne()
            throws Exception {
        kind.addNear(near);

        controller.perform(get("/kinds/get/" + kindRecord.code))
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "kinds-controller-test-get-one-response.json"),
                        true));
    }

    @Disabled("TODO: Implement general 404 handling")
    @Test
    void shouldNotGetOne()
            throws Exception {
        final var code = "KIN";
        when(kinds.byCode(code)).thenReturn(Optional.empty());

        controller.perform(get("/kinds/get/" + code))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldPostOne()
            throws Exception {
        doReturn(fixedKind(nears)).when(kinds).unsaved(
                kindRecord.code, kindRecord.coolness);

        controller.perform(post("/kinds/post")
                .content(from("kinds-controller-test-post-one-request.json"))
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        LOCATION, "/kinds/get/" + kindRecord.code))
                .andExpect(content().json(from(
                        "kinds-controller-test-post-one-response.json"),
                        true));
    }

    private String from(final String jsonFile)
            throws IOException {
        final var loader = new ClassRelativeResourceLoader(getClass());
        // It's a Jedi mind trick
        return new Scanner(
                loader.getResource(jsonFile).readableChannel(), UTF_8)
                .useDelimiter("\\A")
                .next();
    }
}
