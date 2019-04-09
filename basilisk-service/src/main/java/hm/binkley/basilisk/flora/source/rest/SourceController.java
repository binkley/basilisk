package hm.binkley.basilisk.flora.source.rest;

import hm.binkley.basilisk.flora.location.Locations;
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
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.created;

@RequestMapping("/sources")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class SourceController {
    private final Sources sources;
    private final Locations locations;

    @GetMapping
    public SortedSet<SourceResponse> getAll() {
        return sources.all()
                .map(SourceResponse::of)
                .collect(toCollection(TreeSet::new));
    }

    @GetMapping("{id}")
    public SourceResponse getById(
            @PathVariable("id") final Long id) {
        return sources.byId(id)
                .map(SourceResponse::of)
                .orElseThrow();
    }

    @GetMapping("find/{name}")
    public SourceResponse getByName(
            @PathVariable("name") final @Length(min = 3, max = 32)
                    String name) {
        return sources.byName(name)
                .map(SourceResponse::of)
                .orElseThrow();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<SourceResponse> postSource(
            @RequestBody final @Valid SourceRequest request) {
        final var source = sources
                .unsaved(request.getCode(), request.getName());
        request.getAvailableAt().forEach(location -> source.addAvailableAt(
                locations.unsaved(location.getCode(), location.getName())));
        source.save();
        final var response = SourceResponse.of(source);

        return created(URI.create("/sources/" + response.getId()))
                .body(response);
    }

    /**
     * @todo Return 422 or other than 404 for bad child elements
     * @todo Think more deeply about global controller advice
     */
    @ExceptionHandler({
            NoSuchElementException.class,
            MethodArgumentTypeMismatchException.class})
    @ResponseStatus(NOT_FOUND)
    @SuppressWarnings("PMD")
    private void handleNotFound() {
    }
}
