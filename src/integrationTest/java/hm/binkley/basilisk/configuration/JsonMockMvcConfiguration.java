package hm.binkley.basilisk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Configuration
public class JsonMockMvcConfiguration {
    @Bean
    @Primary
    public MockMvc jsonMockMvc(final WebApplicationContext ctx) {
        return webAppContextSetup(ctx)
                .defaultRequest(post("/")
                        .contentType(APPLICATION_JSON_UTF8)
                        .accept(APPLICATION_JSON_UTF8_VALUE))
                .alwaysExpect(header().string(
                        CONTENT_TYPE, APPLICATION_JSON_UTF8_VALUE))
                .build();
    }
}
