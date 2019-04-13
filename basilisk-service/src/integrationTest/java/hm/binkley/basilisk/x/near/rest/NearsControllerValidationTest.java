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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static hm.binkley.basilisk.TestJson.readTestJson;
import static hm.binkley.basilisk.x.TestFixtures.fixedNear;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
                .content(readTestJson("request"))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }

    @Test
    void shouldComplainOnPutOneInvalid()
            throws Exception {
        controller.perform(put("/nears/put/" + nearCode)
                .content(readTestJson("request"))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }
}
