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
class SourceRepositoryTest {
    private final SourceRepository repository;

    @Test
    void shouldAudit() {
        final var unsaved = SourceRecord.raw("PICKLES");
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found.getReceivedAt()).isNotNull();
    }

    @Test
    void shouldRoundtrip() {
        final var unsaved = SourceRecord.raw("EGGS");
        final var found = repository.findById(
                repository.save(unsaved).getId()).orElseThrow();

        assertThat(found).isEqualTo(unsaved);
    }

    @Test
    void shouldHaveUniqueName() {
        final var name = "BACON";
        repository.save(SourceRecord.raw(name));

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(SourceRecord.raw(name)));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldFindByName() {
        final var unsavedLeft = SourceRecord.raw("BUTTER");
        final var unsavedRight = SourceRecord.raw("SALT");
        repository.saveAll(Set.of(unsavedLeft, unsavedRight));

        assertThat(repository.findByName(unsavedLeft.getName()).orElseThrow())
                .isEqualTo(unsavedLeft);
        assertThat(repository.findByName("OLIVE OIL")).isEmpty();
    }

    @Test
    void shouldStream() {
        final var unsavedA = SourceRecord.raw("MILK");
        final var unsavedB = SourceRecord.raw("SALT");
        repository.saveAll(Set.of(unsavedA, unsavedB));

        try (final var found = repository.readAll()) {
            assertThat(found).containsOnly(unsavedA, unsavedB);
        }
    }
}
