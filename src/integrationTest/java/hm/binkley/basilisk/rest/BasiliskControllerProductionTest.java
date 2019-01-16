package hm.binkley.basilisk.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BasiliskController.class)
@TestPropertySource(properties = "server.error.include-stacktrace=never")
class BasiliskControllerProductionTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BasiliskRepository repository;
    @MockBean
    private BasiliskService service;
    @Autowired
    private ServerProperties serverProperties;

    @Test
    void shouldShowStackTraceWhenRequired()
            throws Exception {
        mvc.perform(post("/basilisk")
                .contentType(APPLICATION_JSON_UTF8)
                .content(asJson(BasiliskRequest.builder()
                        .word("FOO")
                        .when(null)
                        .build()))
                .param("trace", "true"))
                .andExpect(status().isIAmATeapot())
                .andExpect(jsonPath("$.exception").exists())
                .andExpect(jsonPath("$.trace").doesNotExist());

        verifyNoMoreInteractions(repository, service);
    }

    private String asJson(final Object o)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
