package hm.binkley.basilisk.flora.location.store;

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

import static hm.binkley.basilisk.flora.FloraFixtures.unsavedLocationRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@Import(DatabaseConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class LocationRepositoryTest {
    private final LocationRepository repository;

    private static LocationRecord distinctLocationRecord() {
        final var record = unsavedLocationRecord();
        return LocationRecord.unsaved(
                record.getCode() + "x", record.getName() + "x");
    }

    @Test
    void shouldAudit() {
        final var unsaved = unsavedLocationRecord();
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found.getReceivedAt()).isNotNull();
    }

    @Test
    void shouldRoundTrip() {
        final var unsaved = unsavedLocationRecord();
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found).isEqualTo(unsaved);
    }

    @Test
    void shouldFindByCode() {
        final var unsaved = unsavedLocationRecord();
        repository.save(unsaved);

        assertThat(repository.findByCode(unsaved.getCode()).orElseThrow())
                .isEqualTo(unsaved);
        assertThat(repository.findByCode(distinctLocationRecord().getCode()))
                .isEmpty();
    }

    @Test
    void shouldHaveUniqueCode() {
        repository.save(unsavedLocationRecord());

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(unsavedLocationRecord()));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldFindByName() {
        final var unsaved = unsavedLocationRecord();
        repository.save(unsaved);

        assertThat(repository.findByName(unsaved.getName()).orElseThrow())
                .isEqualTo(unsaved);
        assertThat(repository.findByName(distinctLocationRecord().getName()))
                .isEmpty();
    }

    @Test
    void shouldHaveUniqueName() {
        repository.save(unsavedLocationRecord());

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(unsavedLocationRecord()));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }
}
