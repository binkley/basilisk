package hm.binkley.basilisk.x.near.rest;

import hm.binkley.basilisk.configuration.JsonWebMvcTest;
import hm.binkley.basilisk.x.near.Near;
import hm.binkley.basilisk.x.near.Nears;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@JsonWebMvcTest(NearsController.class)
class NearsControllerTest {
    private final MockMvc controller;

    @MockBean
    private Nears nears;

    private String nearCode;
    private Near near;

    @BeforeEach
    void setUp() {
        final var near = fixedNear();
        nearCode = near.getCode();
        this.near = spy(near);
        lenient().doReturn(Optional.of(this.near))
                .when(nears).byCode(nearCode);
    }

    @Test
    void shouldGetNone()
            throws Exception {
        when(nears.all()).thenReturn(Stream.empty());

        controller.perform(get("/nears/get"))
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "nears-controller-test-get-none-response.json"),
                        true));
    }

    @Test
    void shouldGetAll()
            throws Exception {
        when(nears.all()).thenReturn(Stream.of(near));

        controller.perform(get("/nears/get"))
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "nears-controller-test-get-all-response.json"),
                        true));
    }

    @Test
    void shouldGetOne()
            throws Exception {
        controller.perform(get("/nears/get/" + near.getCode()))
                .andExpect(status().isOk())
                .andExpect(content().json(from(
                        "nears-controller-test-get-one-response.json"),
                        true));
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
        doReturn(near).when(nears).unsaved(nearCode);
        doReturn(near).when(near).save();

        controller.perform(post("/nears/post")
                .content(from("nears-controller-test-post-one-request.json"))
                .contentType(APPLICATION_JSON_UTF8)) // TODO: Waste of typing
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        LOCATION, "/nears/get/" + nearCode))
                .andExpect(content().json(from(
                        "nears-controller-test-post-one-response.json"),
                        true));

        verify(near).save();
    }

    @Test
    void shouldDeleteOne()
            throws Exception {
        doReturn(near).when(near).delete();

        controller.perform(delete("/nears/delete/" + nearCode))
                .andExpect(status().isNoContent());

        verify(near).delete();
    }

    @Disabled("TODO: Implement general 404 handling")
    @Test
    void shouldNotDeleteOne()
            throws Exception {
        final var code = "ABC";
        when(nears.byCode(code)).thenReturn(Optional.empty());

        controller.perform(delete("/nears/delete/" + code))
                .andExpect(status().isNotFound());

        verify(near, never()).delete();
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
