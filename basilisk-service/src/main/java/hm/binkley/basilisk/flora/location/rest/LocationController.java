package hm.binkley.basilisk.flora.location.rest;

import hm.binkley.basilisk.flora.location.Location;
import hm.binkley.basilisk.flora.location.Locations;
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

@RequestMapping("/locations")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class LocationController {
    private final Locations locations;

    public static LocationResponse toResponse(final Location location) {
        return new LocationResponse(location.getId(), location.getCode(),
                location.getName());
    }

    @GetMapping
    public SortedSet<LocationResponse> getAll() {
        return locations.all()
                .map(LocationController::toResponse)
                .collect(toCollection(TreeSet::new));
    }

    @GetMapping("{id}")
    public LocationResponse getById(
            @PathVariable("id") final Long id) {
        return locations.byId(id)
                .map(LocationController::toResponse)
                .orElseThrow();
    }

    @GetMapping("with-name/{name}")
    public LocationResponse getByName(
            @PathVariable("name") final @Length(min = 3, max = 32)
                    String name) {
        return locations.byName(name)
                .map(LocationController::toResponse)
                .orElseThrow();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<LocationResponse> postLocation(
            @RequestBody final @Valid LocationRequest request) {
        final var domain = locations
                .unsaved(request.getCode(), request.getName())
                .save();
        final var response = toResponse(domain);

        return created(URI.create("/locations/" + response.getId()))
                .body(response);
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
