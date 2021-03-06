package hm.binkley.basilisk.flora.source.store;

import hm.binkley.basilisk.configuration.DatabaseConfiguration;
import hm.binkley.basilisk.flora.source.store.SourceRecord.LocationRef;
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

import static hm.binkley.basilisk.flora.FloraFixtures.savedLocationRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedSourceRecord;
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
    void shouldFindByCode() {
        final var unsavedA = unsavedSourceRecord();
        final var unsavedB = distinctSourceRecord();
        repository.saveAll(Set.of(unsavedA, unsavedB));

        assertThat(repository.findByCode(unsavedA.getCode()).orElseThrow())
                .isEqualTo(unsavedA);
        assertThat(repository.findByCode("OLIVE OIL")).isEmpty();
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
    void shouldSaveLocationRef() {
        final var locationRef = LocationRef.of(savedLocationRecord());
        final var unsaved = unsavedSourceRecord();
        unsaved.getAvailableAt().add(locationRef);

        final var saved = repository.save(unsaved);
        final var readBack = repository
                .findById(saved.getId())
                .orElseThrow();

        assertThat(readBack.getAvailableAt()).containsExactly(locationRef);
    }
}
