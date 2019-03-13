package hm.binkley.basilisk.flora.domain.store;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@Import(DatabaseConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class RecipeRepositoryTest {
    private static final Long CHEF_ID = 17L;

    private final RecipeRepository repository;

    private static <T> T first(final Iterable<T> children) {
        final var it = children.iterator();
        assertThat(it.hasNext())
                .withFailMessage("No children")
                .isTrue();
        return it.next();
    }

    @Test
    void shouldAudit() {
        final var unsaved = RecipeRecord.raw("MERINGUE", CHEF_ID);
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found.getReceivedAt()).isNotNull();
    }

    @Test
    void shouldSaveWithNoIngredients() {
        final var unsaved = RecipeRecord.raw("SOUFFLE", CHEF_ID);
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found).isEqualTo(unsaved);
    }

    @Test
    void shouldSaveWithSomeIngredients() {
        final var unsaved = RecipeRecord.raw("SOUFFLE", CHEF_ID)
                .add(IngredientRecord.raw("EGGS", CHEF_ID));

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
    void shouldHaveUniqueName() {
        final var name = "SOUFFLE";
        repository.save(RecipeRecord.raw(name, CHEF_ID));

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(RecipeRecord.raw(name, CHEF_ID)));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldFindByName() {
        final var unsavedLeft = RecipeRecord.raw("BOILED EGGS", CHEF_ID);
        final var unsavedRight = RecipeRecord.raw("POACHED EGGS", CHEF_ID);
        repository.saveAll(Set.of(unsavedLeft, unsavedRight));

        assertThat(repository.findByName(unsavedLeft.getName()).orElseThrow())
                .isEqualTo(unsavedLeft);
        assertThat(repository.findByName("FRIED EGGS")).isEmpty();
    }

    @Test
    void shouldStream() {
        final var unsavedA = RecipeRecord.raw("SCRAMBLED EGGS", CHEF_ID);
        final var unsavedB = RecipeRecord.raw("BOILED EGGS", CHEF_ID);
        repository.saveAll(Set.of(unsavedA, unsavedB));

        try (final var found = repository.readAll()) {
            assertThat(found).containsOnly(unsavedA, unsavedB);
        }
    }
}
