package hm.binkley.basilisk.x.kind.rest;

import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.configuration.ProblemConfiguration;
import hm.binkley.basilisk.configuration.ProblemWebMvcTest;
import hm.binkley.basilisk.x.kind.Kind;
import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.near.Nears;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static hm.binkley.basilisk.TestJson.readTestJsonRequest;
import static hm.binkley.basilisk.TestJson.readTestJsonResponse;
import static hm.binkley.basilisk.x.TestFixtures.fixedKind;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({JsonConfiguration.class, ProblemConfiguration.class})
@ProblemWebMvcTest(KindsController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class KindsControllerValidationTest {
    private final MockMvc controller;

    @MockBean
    private Kinds kinds;
    @MockBean
    private Nears nears;

    private String kindCode;
    private Kind kind;

    @BeforeEach
    void setUp() {
        final var fixedKind = fixedKind(nears);
        kindCode = fixedKind.getCode();
        kind = spy(fixedKind);
        lenient().doReturn(Optional.of(kind))
                .when(kinds).byCode(kindCode);
    }

    @Test
    void shouldComplainOnGetOneNotFound()
            throws Exception {
        when(kinds.byCode(kindCode))
                .thenReturn(Optional.empty());

        controller.perform(get("/kinds/get/" + kindCode))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(content().json(readTestJsonResponse(), true));
    }

    @Test
    void shouldComplainOnPostOneInvalid()
            throws Exception {
        controller.perform(post("/kinds/post")
                .content(readTestJsonRequest())
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(content().json(readTestJsonResponse(), true));
    }

    @Test
    void shouldComplainOnPutOneInvalid()
            throws Exception {
        controller.perform(put("/kinds/put/" + kindCode)
                .content(readTestJsonRequest())
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(content().json(readTestJsonResponse(), true));
    }

    @Test
    void shouldComplainOnPostNearsAddInvalid()
            throws Exception {
        controller.perform(post("/kinds/post/" + kindCode + "/nears/add")
                .content(readTestJsonRequest())
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(content().json(readTestJsonResponse(), true));
    }

    @Test
    void shouldComplainOnPostNearsRemoveInvalid()
            throws Exception {
        controller.perform(post("/kinds/post/" + kindCode + "/nears/remove")
                .content(readTestJsonRequest())
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(content().json(readTestJsonResponse(), true));
    }

    @Test
    void shouldComplainOnDeleteNotFound()
            throws Exception {
        when(kinds.byCode(kindCode))
                .thenReturn(Optional.empty());

        controller.perform(delete("/kinds/delete/" + kindCode))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON))
                .andExpect(content().json(readTestJsonResponse(), true));
    }
}
