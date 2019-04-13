package hm.binkley.basilisk.x.near.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.lang.StackWalker.StackFrame;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

import static hm.binkley.basilisk.x.TestFixtures.fixedNear;
import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;
import static java.nio.charset.StandardCharsets.UTF_8;
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
    private final ObjectMapper objectMapper;

    @MockBean
    private Nears nears;

    private String nearCode;

    private static String from(final String jsonFile)
            throws IOException {
        final var stackWalker
                = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);
        final var testClass = stackWalker.getCallerClass();

        final var loader = new ClassRelativeResourceLoader(testClass);
        // It's a Jedi mind trick
        return new Scanner(
                loader.getResource(jsonFile).readableChannel(), UTF_8)
                .useDelimiter("\\A")
                .next();
    }

    private static String fromX(final String tag)
            throws IOException {
        final var stackWalker
                = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);
        final StackFrame testFrame = stackWalker.walk(stacks -> stacks
                .filter(it -> it.getMethodName().startsWith("should"))
                .findFirst()
                .orElseThrow());
        final var testClass = testFrame.getDeclaringClass();

        final var pattern = Pattern.compile("([A-Z])");
        var matcher = pattern.matcher(testClass.getSimpleName());
        final var filePrefix = matcher
                .replaceAll(mr -> "-" + mr.group(1).toLowerCase())
                .substring(1);
        matcher = pattern.matcher(testFrame.getMethodName());
        final var fileSuffix = matcher
                .replaceAll(mr -> "-" + mr.group(1).toLowerCase())
                .substring("should-".length());
        final var jsonFile
                = filePrefix + "-" + fileSuffix + "-" + tag + ".json";

        // It's a Jedi mind trick
        return new Scanner(new ClassRelativeResourceLoader(testClass)
                .getResource(jsonFile)
                .readableChannel(), UTF_8)
                .useDelimiter("\\A")
                .next();
    }

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
                .content(fromX("request"))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                        .contentType(APPLICATION_PROBLEM_JSON));
    }

    @Test
    void shouldComplainOnPutOneInvalid()
            throws Exception {
        controller.perform(put("/nears/put/" + nearCode)
                .content(fromX("request"))
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content()
                        .contentType(APPLICATION_PROBLEM_JSON));
    }
}
