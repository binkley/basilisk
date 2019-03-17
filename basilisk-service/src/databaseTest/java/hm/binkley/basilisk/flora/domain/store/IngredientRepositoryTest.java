package hm.binkley.basilisk.flora.domain.store;

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

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedUnusedIngredientRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedUnusedIngredientRecordNamed;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@Import(DatabaseConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class IngredientRepositoryTest {
    private final IngredientRepository repository;

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
        final var unsavedB = unsavedUnusedIngredientRecordNamed(
                SOURCE_ID + 1, "PICKLES");
        repository.saveAll(Set.of(unsavedA, unsavedB));

        assertThat(repository.findAllByName(unsavedA.getName()))
                .containsExactly(unsavedA);
        assertThat(repository.findAllByName("OLIVE OIL")).isEmpty();
    }

    @Test
    void shouldStream() {
        final var unsavedA = unsavedUnusedIngredientRecord();
        final var unsavedB = unsavedUnusedIngredientRecordNamed(
                SOURCE_ID + 1, "PICKLES");
        repository.saveAll(Set.of(unsavedA, unsavedB));

        try (final var found = repository.readAll()) {
            assertThat(found).containsOnly(unsavedA, unsavedB);
        }
    }
}
