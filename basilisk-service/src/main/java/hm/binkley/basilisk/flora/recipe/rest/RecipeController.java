package hm.binkley.basilisk.flora.recipe.rest;

import hm.binkley.basilisk.flora.recipe.Recipe;
import hm.binkley.basilisk.flora.recipe.Recipes;
import hm.binkley.basilisk.flora.service.SpecialService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
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

@RequestMapping("/recipes")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class RecipeController {
    static final String CREATED_RECIPE = "Created a new recipe: {}";
    private final Recipes recipes;
    private final SpecialService specialService;
    private final Logger logger;

    RecipeResponse toResponse(final Recipe domain) {
        return RecipeResponse.of(
                domain, specialService.isDailySpecial(domain));
    }

    @GetMapping
    public SortedSet<RecipeResponse> getAll() {
        return recipes.all()
                .map(this::toResponse)
                .collect(toCollection(TreeSet::new));
    }

    @GetMapping("{id}")
    public RecipeResponse getById(@PathVariable("id") final Long id) {
        return recipes.byId(id)
                .map(this::toResponse)
                .orElseThrow();
    }

    @GetMapping("find/{name}")
    public RecipeResponse getByName(
            @PathVariable("name") final @Length(min = 3, max = 32)
                    String name) {
        return recipes.byName(name)
                .map(this::toResponse)
                .orElseThrow();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<RecipeResponse> postRecipe(
            @RequestBody final @Valid RecipeRequest request) {
        final var domain = recipes.create(request);
        final var response = toResponse(domain);
        logger.info(CREATED_RECIPE, response);

        return created(URI.create("/recipes/" + response.getId()))
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
