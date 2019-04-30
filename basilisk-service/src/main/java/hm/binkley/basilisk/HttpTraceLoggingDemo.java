package hm.binkley.basilisk;

import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * To see HTTP trace logging in the live program, set {@code
 * org.zalando.logbook.Logbook} logging to "TRACE", and uncomment
 * {@code @Component} before running.
 */
// @Component
@Generated // Lie to JaCoCo
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HttpTraceLoggingDemo
        implements CommandLineRunner {
    private final JsonPlaceholder jsonPlaceholder;

    @Override
    public void run(final String... args)
            throws Exception {
        final var request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/nears/get"))
                .header("Accept", "application/json")
                .build();
        final var client = HttpClient.newBuilder()
                .build();

        client.send(request, BodyHandlers.ofString());

        jsonPlaceholder.getTodo(1);
    }
}
