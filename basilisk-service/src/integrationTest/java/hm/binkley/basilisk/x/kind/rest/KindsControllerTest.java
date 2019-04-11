package hm.binkley.basilisk.x.kind.rest;

import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.x.kind.Kind;
import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.near.Near;
import hm.binkley.basilisk.x.near.Nears;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import static hm.binkley.basilisk.x.TestFixtures.fixedKind;
import static hm.binkley.basilisk.x.TestFixtures.fixedNear;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private String nearCode;
    private Kind kind;
    private String kindCode;
    private BigDecimal kindCoolness;

    @BeforeEach
    void setUp() {
        near = fixedNear();
        nearCode = near.getCode();
        near = spy(near);
        kind = fixedKind(nears);
        kindCode = kind.getCode();
        kindCoolness = kind.getCoolness();
        kind = spy(kind);

        lenient().doReturn(Optional.of(near))
                .when(nears).byCode(nearCode);
        lenient().doReturn(Optional.of(kind))
                .when(kinds).byCode(kindCode);
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

        controller.perform(get("/kinds/get/" + kindCode))
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "kinds-controller-test-get-one-response.json"),
                        true));
    }

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
        doReturn(kind).when(kinds).unsaved(kindCode, kindCoolness);

        controller.perform(post("/kinds/post")
                .content(from("kinds-controller-test-post-one-request.json"))
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        LOCATION, "/kinds/get/" + kindCode))
                .andExpect(content().json(from(
                        "kinds-controller-test-post-one-response.json"),
                        true));

        verify(kind).save();
    }

    @Test
    void shouldPutOneNew()
            throws Exception {
        doReturn(Optional.empty()).when(kinds).byCode(kindCode);
        doReturn(kind).when(kinds).unsaved(kindCode, kindCoolness);
        doReturn(kind).when(kind).save();

        controller.perform(put("/kinds/put/" + kindCode)
                .content(from(
                        "kinds-controller-test-put-one-new-request.json"))
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isCreated())
                .andExpect(content().json(from(
                        "kinds-controller-test-put-one-new-response.json"),
                        true));

        verify(kind).save();
    }

    @Test
    void shouldPutOneExisting()
            throws Exception {
        doReturn(kind).when(kinds).unsaved(kindCode, kindCoolness);
        doReturn(kind).when(kind).save();

        controller.perform(put("/kinds/put/" + kindCode)
                .content(from(
                        "kinds-controller-test-put-one-existing-request"
                                + ".json"))
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "kinds-controller-test-put-one-existing-response"
                                + ".json"),
                        true));

        verify(kind).save();
    }

    @Test
    void shouldDeleteOne()
            throws Exception {
        doReturn(kind).when(kind).delete();

        controller.perform(delete("/kinds/delete/" + kindCode))
                .andExpect(status().isNoContent());

        verify(kind).delete();
    }

    @Test
    void shouldNotDeleteOne()
            throws Exception {
        final var code = "ABC";
        when(kinds.byCode(code)).thenReturn(Optional.empty());

        controller.perform(delete("/kinds/delete/" + code))
                .andExpect(status().isNotFound());

        verify(kind, never()).delete();
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
