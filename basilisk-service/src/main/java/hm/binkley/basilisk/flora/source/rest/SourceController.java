package hm.binkley.basilisk.flora.source.rest;

import hm.binkley.basilisk.flora.location.rest.LocationResponse;
import hm.binkley.basilisk.flora.source.Source;
import hm.binkley.basilisk.flora.source.Sources;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toSet;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.created;

@RequestMapping("/source")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class SourceController {
    private final Sources sources;

    @GetMapping
    public Set<SourceResponse> getAll() {
        return sources.all()
                .map(toResponse())
                .collect(toSet());
    }

    @GetMapping("{id}")
    public SourceResponse getById(
            @PathVariable("id") final Long id) {
        return sources.byId(id)
                .map(toResponse())
                .orElseThrow();
    }

    @GetMapping("find/{name}")
    public SourceResponse getByName(
            @PathVariable("name") final @Length(min = 3, max = 32)
                    String name) {
        return sources.byName(name)
                .map(toResponse())
                .orElseThrow();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<SourceResponse> postSource(
            @RequestBody final @Valid SourceRequest request) {
        final var domain = sources.create(request);
        final var response = toResponse().apply(domain);

        return created(URI.create("/source/" + response.getId()))
                .body(response);
    }

    private Function<Source, SourceResponse> toResponse() {
        return it -> it.as(SourceResponse.using(), LocationResponse.using());
    }

    /** @todo Think more deeply about global controller advice */
    @ExceptionHandler({
            NoSuchElementException.class,
            MethodArgumentTypeMismatchException.class})
    @ResponseStatus(NOT_FOUND)
    @SuppressWarnings("PMD")
    private void handleNotFound() {
    }
}
