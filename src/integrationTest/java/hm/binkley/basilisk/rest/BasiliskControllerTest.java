package hm.binkley.basilisk.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRecord;
import hm.binkley.basilisk.store.BasiliskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static java.time.ZoneOffset.UTC;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class BasiliskControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BasiliskRepository repository;
    @MockBean
    private BasiliskService service;

    @Test
    void shouldAcceptWords()
            throws Exception {
        final String word = "foo";
        when(repository.findByWord(word))
                .thenReturn(List.of(BasiliskRecord.builder()
                        .id(1L)
                        .receivedAt(Instant.EPOCH)
                        .word(word)
                        .when(OffsetDateTime.of(
                                2011, 2, 3, 4, 5, 6, 7_000_000, UTC)
                                .toInstant())
                        .build()));
        when(service.extra(word))
                .thenReturn("Margaret Hamilton");

        mvc.perform(get("/basilisk/" + word))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json(objectMapper.writeValueAsString(
                        List.of(Map.of("word", word,
                                "when", "2011-02-03T04:05:06.007Z",
                                "extra", "Margaret Hamilton")))));
    }
}
