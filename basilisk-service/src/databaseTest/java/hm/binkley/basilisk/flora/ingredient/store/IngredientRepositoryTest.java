package hm.binkley.basilisk.flora.ingredient.store;

import hm.binkley.basilisk.configuration.DatabaseConfiguration;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static hm.binkley.basilisk.flora.FloraFixtures.unsavedUnusedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@Import(DatabaseConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class IngredientRepositoryTest {
    private final IngredientRepository repository;

    private static IngredientRecord distinctIngredientRecord() {
        final var record = unsavedUnusedIngredientRecord();
        return IngredientRecord.unsaved(
                record.getCode() + "x", record.getSourceId(),
                record.getName() + "x", record.getQuantity(),
                record.getChefId());
    }

    @Test
    void shouldAudit() {
        final var unsaved = unsavedUnusedIngredientRecord();
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found.getReceivedAt()).isNotNull();
    }

    @Test
    void shouldRoundTrip() {
        final var unsaved = unsavedUnusedIngredientRecord();
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found).isEqualTo(unsaved);
    }

    @Test
    void shouldFindAllByName() {
        final var unsavedA = unsavedUnusedIngredientRecord();
        final var unsavedB = distinctIngredientRecord();
        repository.saveAll(Set.of(unsavedA, unsavedB));

        assertThat(repository.findAllByName(unsavedA.getName()))
                .containsExactly(unsavedA);
        assertThat(repository.findAllByName("OLIVE OIL")).isEmpty();
    }
}
