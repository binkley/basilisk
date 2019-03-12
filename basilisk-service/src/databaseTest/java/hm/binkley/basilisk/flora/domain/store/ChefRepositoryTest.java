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
class ChefRepositoryTest {
    private static final String NAME = "The Dallas Yellow Rose";

    private final ChefRepository repository;

    @Test
    void shouldRoundtrip() {
        final var unsaved = ChefRecord.raw(NAME);
        final var found = repository.findById(
                repository.save(unsaved).getId());

        assertThat(found).contains(unsaved);
    }

    @Test
    void shouldHaveUniqueName() {
        repository.save(ChefRecord.raw(NAME));

        final var ex = assertThrows(
                DbActionExecutionException.class,
                () -> repository.save(ChefRecord.raw(NAME)));

        assertThat(ex.getCause()).isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void shouldFindByName() {
        final var unsaved = ChefRecord.raw(NAME);
        repository.save(unsaved);

        assertThat(repository.findByName(unsaved.getName()).orElseThrow())
                .isEqualTo(unsaved);
        assertThat(repository.findByName("Melbourne's Pink Heath")).isEmpty();
    }

    @Test
    void shouldStream() {
        final var unsavedA = ChefRecord.raw(NAME);
        final var unsavedB = ChefRecord.raw(
                "Melbourne's Pink Heath");
        repository.saveAll(Set.of(unsavedA, unsavedB));

        try (final var found = repository.readAll()) {
            assertThat(found).containsOnly(unsavedA, unsavedB);
        }
    }
}
