package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Location;
import hm.binkley.basilisk.flora.domain.Locations;
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

@RequestMapping("/location")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class LocationController {
    private final Locations locations;

    @GetMapping
    public Set<LocationResponse> getAll() {
        return locations.all()
                .map(toResponse())
                .collect(toSet());
    }

    @GetMapping("{id}")
    public LocationResponse getById(
            @PathVariable("id") final Long id) {
        return locations.byId(id)
                .map(toResponse())
                .orElseThrow();
    }

    @GetMapping("with-name/{name}")
    public LocationResponse getByName(
            @PathVariable("name") final @Length(min = 3, max = 32)
                    String name) {
        return locations.byName(name)
                .map(toResponse())
                .orElseThrow();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<LocationResponse> postLocation(
            @RequestBody final @Valid LocationRequest request) {
        final var domain = locations.create(request);
        final var response = toResponse().apply(domain);

        return created(URI.create("/location/" + response.getId()))
                .body(response);
    }

    private Function<Location, LocationResponse> toResponse() {
        return it -> it.as(LocationResponse.using());
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
