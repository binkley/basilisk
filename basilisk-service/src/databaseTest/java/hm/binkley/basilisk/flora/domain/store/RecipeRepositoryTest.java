package hm.binkley.basilisk.flora.domain.store;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class RecipeRepositoryTest {
    private final RecipeRepository repository;

    private static <T> T first(final Iterable<T> children) {
        final var it = children.iterator();
        assertThat(it.hasNext())
                .withFailMessage("No children")
                .isTrue();
        return it.next();
    }

    @Test
    void shouldSaveWithNoIngredients() {
        final var unsaved = RecipeRecord.raw("SOUFFLE");
        final var found = repository.findById(
                repository.save(unsaved).getId());

        assertThat(found).contains(unsaved);
    }

    @Test
    void shouldSaveWithSomeIngredients() {
        final var unsaved = RecipeRecord.raw("SOUFFLE");
        unsaved.ingredients.add(IngredientRecord.raw("EGGS"));

        final var saved = repository.save(unsaved);
        final var found = repository.findById(saved.getId());
        final var record = found.orElseThrow();

        assertThat(record).isEqualTo(unsaved);
        assertThat(record.getId()).isNotNull();
        assertThat(first(record.getIngredients()).getId())
                .withFailMessage("No ID on children")
                .isNotNull();
    }

    @Test
    void shouldHaveUniqueName() {
        final var name = "SOUFFLE";
        repository.save(RecipeRecord.raw(name));

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(RecipeRecord.raw(name)));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldFindByName() {
        final var unsavedLeft = RecipeRecord.raw("BOILED EGGS");
        final var unsavedRight = RecipeRecord.raw("POACHED EGGS");
        repository.saveAll(Set.of(unsavedLeft, unsavedRight));

        assertThat(repository.findByName(unsavedLeft.getName()).orElseThrow())
                .isEqualTo(unsavedLeft);
        assertThat(repository.findByName("FRIED EGGS")).isEmpty();
    }

    @Test
    void shouldStream() {
        final var unsavedA = RecipeRecord.raw("SCRAMBLED EGGS");
        final var unsavedB = RecipeRecord.raw("BOILED EGGS");
        repository.saveAll(Set.of(unsavedA, unsavedB));

        try (final var found = repository.readAll()) {
            assertThat(found).containsOnly(unsavedA, unsavedB);
        }
    }
}
