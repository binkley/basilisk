package hm.binkley.basilisk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.resolve;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Configuration
public class JsonMockMvcConfiguration {
    private static final Set<HttpStatus> noBodies
            = Set.of(NO_CONTENT, NOT_FOUND);

    @Bean
    @Primary
    public MockMvc jsonMockMvc(final WebApplicationContext ctx) {
        return webAppContextSetup(ctx)
                .defaultRequest(post("/")
                        .contentType(APPLICATION_JSON_UTF8)
                        .accept(APPLICATION_JSON_UTF8_VALUE))
                .alwaysDo(result -> {
                    if (!noBodies.contains(
                            resolve(result.getResponse().getStatus())))
                        content().contentType(APPLICATION_JSON_UTF8)
                                .match(result);
                })
                .build();
    }
}
