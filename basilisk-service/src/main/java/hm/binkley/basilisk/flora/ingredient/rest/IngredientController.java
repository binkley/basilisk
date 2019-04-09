package hm.binkley.basilisk.flora.ingredient.rest;

import hm.binkley.basilisk.flora.ingredient.Ingredients;
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

@RequestMapping("/ingredients")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class IngredientController {
    private final Ingredients ingredients;

    @GetMapping
    public SortedSet<AnyIngredientResponse> getAll() {
        return ingredients.all()
                .map(AnyIngredientResponse::of)
                .collect(toCollection(TreeSet::new));
    }

    @GetMapping("{id}")
    public AnyIngredientResponse getById(
            @PathVariable("id") final Long id) {
        return ingredients.byId(id)
                .map(AnyIngredientResponse::of)
                .orElseThrow();
    }

    @GetMapping("find/{name}")
    public SortedSet<AnyIngredientResponse> getAllByName(
            @PathVariable("name") final @Length(min = 3, max = 32)
                    String name) {
        return ingredients.allByName(name)
                .map(AnyIngredientResponse::of)
                .collect(toCollection(TreeSet::new));
    }

    @GetMapping("unused")
    public SortedSet<UnusedIngredientResponse> getAllUnused() {
        return ingredients.allUnused()
                .map(UnusedIngredientResponse::of)
                .collect(toCollection(TreeSet::new));
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<UnusedIngredientResponse> postIngredient(
            @RequestBody final @Valid UnusedIngredientRequest request) {
        final var ingredient = ingredients.unsaved(
                request.getCode(), request.getSourceId(), request.getName(),
                request.getQuantity(), request.getChefId());
        ingredient.save();
        final var response = UnusedIngredientResponse.of(ingredient);

        return created(URI.create("/ingredients/" + response.getId()))
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
