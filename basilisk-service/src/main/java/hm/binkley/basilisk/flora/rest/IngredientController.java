package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Ingredient;
import hm.binkley.basilisk.flora.domain.Ingredients;
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
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.created;

@RequestMapping("/ingredient")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class IngredientController {
    private final Ingredients ingredients;

    @GetMapping
    public List<IngredientResponse> getAll() {
        return ingredients.all()
                .map(toResponse())
                .collect(toList());
    }

    @GetMapping("{id}")
    public IngredientResponse findById(
            @PathVariable("id") final Ingredient ingredient) {
        return toResponse().apply(ingredient);
    }

    @GetMapping("find/{name}")
    public List<IngredientResponse> findByName(
            @PathVariable("name") final @Length(min = 3, max = 32)
                    String name) {
        return ingredients.byName(name)
                .map(toResponse())
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<IngredientResponse> postIngredient(
            @RequestBody final @Valid IngredientRequest request) {
        final var ingredient = ingredients.create(request);
        final var response = toResponse().apply(ingredient);

        return created(URI.create("/ingredient/" + response.getId()))
                .body(response);
    }

    private Function<Ingredient, IngredientResponse> toResponse() {
        return it -> it.as(IngredientResponse.using());
    }

    /** @todo Think more deeply about global controller advice */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(NOT_FOUND)
    @SuppressWarnings("PMD")
    private void handleNotFound() {
    }
}
