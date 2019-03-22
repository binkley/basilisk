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

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedLocationRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedSourceRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@Import(DatabaseConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class SourceRepositoryTest {
    private final SourceRepository repository;

    private static SourceRecord distinctSourceRecord() {
        final var record = unsavedSourceRecord();
        return SourceRecord.unsaved(
                record.getCode() + "x", record.getName() + "x");
    }

    @Test
    void shouldAudit() {
        final var unsaved = unsavedSourceRecord();
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found.getReceivedAt()).isNotNull();
    }

    @Test
    void shouldRoundTripWithoutConstraints() {
        final var unsaved = unsavedSourceRecord();
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found).isEqualTo(unsaved);
    }

    @Test
    void shouldRoundTripWithConstraints() {
        final var unsaved = unsavedSourceRecord()
                .addAvailableAt(savedLocationRecord());
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found).isEqualTo(unsaved);
    }

    @Test
    void shouldHaveUniqueName() {
        repository.save(unsavedSourceRecord());

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(unsavedSourceRecord()));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldFindByName() {
        final var unsavedA = unsavedSourceRecord();
        final var unsavedB = distinctSourceRecord();
        repository.saveAll(Set.of(unsavedA, unsavedB));

        assertThat(repository.findByName(unsavedA.getName()).orElseThrow())
                .isEqualTo(unsavedA);
        assertThat(repository.findByName("OLIVE OIL")).isEmpty();
    }

    @Test
    void shouldStream() {
        final var unsavedA = unsavedSourceRecord();
        final var unsavedB = distinctSourceRecord();
        repository.saveAll(Set.of(unsavedA, unsavedB));

        try (final var found = repository.readAll()) {
            assertThat(found).containsOnly(unsavedA, unsavedB);
        }
    }
}
