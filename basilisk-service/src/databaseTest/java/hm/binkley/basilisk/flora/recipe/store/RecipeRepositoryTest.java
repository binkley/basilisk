package hm.binkley.basilisk.flora.recipe.store;

import hm.binkley.basilisk.configuration.DatabaseConfiguration;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static hm.binkley.basilisk.flora.FloraFixtures.unsavedRecipeRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedUnusedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@Import(DatabaseConfiguration.class)
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

    private static RecipeRecord distinctRecipeRecord() {
        final var record = unsavedRecipeRecord();
        return RecipeRecord.unsaved(
                record.getCode() + "x", record.getName() + "x",
                record.getChefId());
    }

    @Test
    void shouldAudit() {
        final var unsaved = unsavedRecipeRecord();
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found.getReceivedAt()).isNotNull();
    }

    @Test
    void shouldSaveWithNoIngredients() {
        final var unsaved = unsavedRecipeRecord();
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found).isEqualTo(unsaved);
    }

    @Test
    void shouldSaveWithSomeIngredients() {
        final var unsaved = unsavedRecipeRecord()
                .add(unsavedUnusedIngredientRecord());

        final var saved = repository.save(unsaved);

        assertThat(saved).isEqualTo(unsaved);
        assertThat(first(saved.getIngredients()).getRecipeId())
                .withFailMessage("Spring Data JDBC now"
                        + " updates children after save; go simplify 'store'"
                        + " in Recipes")
                .isNull();

        final var found = repository.findById(saved.getId());
        final var readBack = found.orElseThrow();
        // See notes in RecipeStore.save()
        first(saved.ingredients).recipeId = readBack.getId();

        assertThat(readBack).isEqualTo(saved);
        assertThat(readBack.getId()).isNotNull();
        assertThat(first(readBack.getIngredients()).getId())
                .withFailMessage("No ID on children")
                .isNotNull();
        assertThat(first(readBack.getIngredients()).getRecipeId())
                .isEqualTo(saved.getId());
    }

    @Test
    void shouldFindByCode() {
        final var unsaved = unsavedRecipeRecord();
        repository.save(unsaved);

        assertThat(repository.findByCode(unsaved.getCode()).orElseThrow())
                .isEqualTo(unsaved);
        assertThat(repository.findByCode(distinctRecipeRecord().getCode()))
                .isEmpty();
    }

    @Test
    void shouldHaveUniqueCode() {
        repository.save(unsavedRecipeRecord());

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(unsavedRecipeRecord()));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldFindByName() {
        final var unsavedA = unsavedRecipeRecord();
        final var unsavedB = distinctRecipeRecord();
        repository.saveAll(Set.of(unsavedA, unsavedB));

        assertThat(repository.findByName(unsavedA.getName()).orElseThrow())
                .isEqualTo(unsavedA);
        assertThat(repository.findByName("FRIED EGGS")).isEmpty();
    }

    @Test
    void shouldHaveUniqueName() {
        repository.save(unsavedRecipeRecord());

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(unsavedRecipeRecord()));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldStream() {
        final var unsavedA = unsavedRecipeRecord();
        final var unsavedB = distinctRecipeRecord();
        repository.saveAll(Set.of(unsavedA, unsavedB));

        try (final var found = repository.readAll()) {
            assertThat(found).containsOnly(unsavedA, unsavedB);
        }
    }
}
