package hm.binkley.basilisk.flora.chef.store;

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

import static hm.binkley.basilisk.flora.FloraFixtures.unsavedChefRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@Import(DatabaseConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class ChefRepositoryTest {
    private final ChefRepository repository;

    private static ChefRecord distinctChefRecord() {
        final var record = unsavedChefRecord();
        return ChefRecord.unsaved(
                record.getCode() + "x", record.getName() + "x");
    }

    @Test
    void shouldAudit() {
        final var unsaved = unsavedChefRecord();
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found.getReceivedAt()).isNotNull();
    }

    @Test
    void shouldRoundTrip() {
        final var unsaved = unsavedChefRecord();
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found).isEqualTo(unsaved);
    }

    @Test
    void shouldFindByCode() {
        final var unsaved = unsavedChefRecord();
        repository.save(unsaved);

        assertThat(repository.findByCode(unsaved.getCode()).orElseThrow())
                .isEqualTo(unsaved);
        assertThat(repository.findByCode(distinctChefRecord().getCode()))
                .isEmpty();
    }

    @Test
    void shouldHaveUniqueCode() {
        repository.save(unsavedChefRecord());

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(unsavedChefRecord()));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldFindByName() {
        final var unsaved = unsavedChefRecord();
        repository.save(unsaved);

        assertThat(repository.findByName(unsaved.getName()).orElseThrow())
                .isEqualTo(unsaved);
        assertThat(repository.findByName(distinctChefRecord().getName()))
                .isEmpty();
    }

    @Test
    void shouldHaveUniqueName() {
        repository.save(unsavedChefRecord());

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(unsavedChefRecord()));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }
}
