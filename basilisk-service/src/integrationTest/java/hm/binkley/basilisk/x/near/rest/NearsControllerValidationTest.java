package hm.binkley.basilisk.x.near.rest;

import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.ProblemWebMvcTest;
import hm.binkley.basilisk.x.near.Nears;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import static hm.binkley.basilisk.x.TestFixtures.fixedNear;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(JsonConfiguration.class)
@ProblemWebMvcTest(NearsController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class NearsControllerValidationTest {
    private final MockMvc controller;

    @MockBean
    private Nears nears;

    private String nearCode;

    @BeforeEach
    void setUp() {
        final var near = fixedNear();
        nearCode = near.getCode();
        lenient().doReturn(Optional.of(spy(near)))
                .when(nears).byCode(nearCode);
    }

    @Test
    void shouldComplainOnPostOneInvalid()
            throws Exception {
        controller.perform(post("/nears/post")
                .content(from(
                        "nears-controller-test-complain-on-post-one"
                                + "-invalid-request.json"))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldComplainOnPutOneInvalid()
            throws Exception {
        controller.perform(put("/nears/put/" + nearCode)
                .content(from(
                        "nears-controller-test-complain-on-put-one"
                                + "-invalid-request.json"))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity());
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
