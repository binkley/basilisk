package hm.binkley.basilisk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Configuration
public class ProblemMockMvcConfiguration {
    @Bean
    @Primary
    public MockMvc problemMockMvc(final WebApplicationContext ctx) {
        return webAppContextSetup(ctx)
                .defaultRequest(post("/")
                        .contentType(APPLICATION_JSON_UTF8)
                        .accept(APPLICATION_JSON_UTF8_VALUE))
                .alwaysDo(result -> {
                    if (NOT_FOUND.value() != result.getResponse().getStatus())
                        // TODO: APPLICATION_PROBLEM_JSON_UTF8
                        content().contentType(APPLICATION_PROBLEM_JSON)
                                .match(result);
                })
                .build();
    }
}
