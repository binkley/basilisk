package hm.binkley.basilisk.flora.chef.rest;

import hm.binkley.basilisk.flora.chef.Chef;
import hm.binkley.basilisk.flora.chef.Chefs;
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

@RequestMapping("/chef")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class ChefController {
    private final Chefs chefs;

    @GetMapping
    public Set<ChefResponse> getAll() {
        return chefs.all()
                .map(toResponse())
                .collect(toSet());
    }

    @GetMapping("{id}")
    public ChefResponse getById(
            @PathVariable("id") final Long id) {
        return chefs.byId(id)
                .map(toResponse())
                .orElseThrow();
    }

    @GetMapping("with-code/{code}")
    public ChefResponse getByCode(
            @PathVariable("code") final @Length(min = 3, max = 32)
                    String code) {
        return chefs.byCode(code)
                .map(toResponse())
                .orElseThrow();
    }

    @GetMapping("with-name/{name}")
    public ChefResponse getByName(
            @PathVariable("name") final @Length(min = 3, max = 32)
                    String name) {
        return chefs.byName(name)
                .map(toResponse())
                .orElseThrow();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<ChefResponse> postChef(
            @RequestBody final @Valid ChefRequest request) {
        final var domain = chefs.create(request);
        final var response = toResponse().apply(domain);

        return created(URI.create("/chef/" + response.getId()))
                .body(response);
    }

    private Function<Chef, ChefResponse> toResponse() {
        return it -> it.as(ChefResponse.using());
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
