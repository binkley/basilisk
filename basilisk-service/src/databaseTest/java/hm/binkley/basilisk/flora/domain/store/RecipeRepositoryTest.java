package hm.binkley.basilisk.flora.domain.store;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class RecipeRepositoryTest {
    private final RecipeRepository repository;
    private final IngredientRepository ingredientRepository;

    private static <T> T first(final Iterable<T> children) {
        final var it = children.iterator();
        assertThat(it.hasNext())
                .withFailMessage("No children")
                .isTrue();
        return it.next();
    }

    @Test
    void shouldSaveWithNoIngredients() {
        final var unsaved = RecipeRecord.raw("EGGS");
        final var found = repository.findById(
                repository.save(unsaved).getId());

        assertThat(found).contains(unsaved);
        assertThat(ingredientRepository.readAll()).isEmpty();
    }

    @Test
    void shouldSaveWithSomeIngredients() {
        final var unsaved = RecipeRecord.raw("SOUFFLE");
        final var ingredientRecord = IngredientRecord.raw("EGGS");
        unsaved.ingredients.add(ingredientRecord);

        final var saved = repository.save(unsaved);
        final var found = repository.findById(saved.getId());
        final var record = found.orElseThrow();

        assertThat(record).isEqualTo(unsaved);
        assertThat(first(record.ingredients).getId())
                .withFailMessage("No ID on children")
                .isNotNull();
        assertThat(ingredientRepository.findById(first(
                found.orElseThrow().getIngredients()).getId()))
                .contains(ingredientRecord);
    }

    @Test
    void shouldFindByName() {
        final var unsavedLeft = RecipeRecord.raw("BOILED EGGS");
        final var unsavedRight = RecipeRecord.raw("POACHED EGGS");
        repository.saveAll(List.of(unsavedLeft, unsavedRight));

        assertThat(repository.findByName(unsavedLeft.getName())
                .collect(toList()))
                .isEqualTo(List.of(unsavedLeft));
        assertThat(repository.findByName("FRIED EGGS")
                .collect(toList()))
                .isEmpty();
    }

    @Test
    void shouldStream() {
        final var unsavedA = RecipeRecord.raw("SCRAMBLED EGGS");
        final var unsavedB = RecipeRecord.raw("BOILED EGGS");
        repository.saveAll(List.of(unsavedA, unsavedB));

        try (final var found = repository.readAll()) {
            assertThat(found).containsExactly(unsavedA, unsavedB);
        }
    }
}
