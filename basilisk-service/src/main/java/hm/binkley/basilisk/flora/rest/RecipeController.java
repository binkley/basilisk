package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Recipe;
import hm.binkley.basilisk.flora.domain.Recipes;
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

@RequestMapping("/recipe")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class RecipeController {
    private final Recipes recipes;

    @GetMapping
    public List<RecipeResponse> getAll() {
        return recipes.all()
                .map(toResponse())
                .collect(toList());
    }

    @GetMapping("{id}")
    public RecipeResponse findById(
            @PathVariable("id") final Recipe recipe) {
        return toResponse().apply(recipe);
    }

    @GetMapping("find/{name}")
    public List<RecipeResponse> findByName(
            @PathVariable("name") final @Length(min = 3, max = 32)
                    String name) {
        return recipes.byName(name)
                .map(toResponse())
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<RecipeResponse> postRecipe(
            @RequestBody final @Valid RecipeRequest request) {
        final var domain = recipes.create(request);
        final var response = toResponse().apply(domain);

        return created(URI.create("/recipe/" + response.getId()))
                .body(response);
    }

    private Function<Recipe, RecipeResponse> toResponse() {
        return it -> it.as(RecipeResponse.using());
    }

    /** @todo Think more deeply about global controller advice */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(NOT_FOUND)
    @SuppressWarnings("PMD")
    private void handleNotFound() {
    }
}
