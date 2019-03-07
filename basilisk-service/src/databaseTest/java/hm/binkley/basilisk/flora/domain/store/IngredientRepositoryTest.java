package hm.binkley.basilisk.flora.domain.store;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class IngredientRepositoryTest {
    private final IngredientRepository repository;

    @Test
    void shouldRoundtrip() {
        final var unsaved = IngredientRecord.raw("EGGS");
        final var found = repository.findById(
                repository.save(unsaved).getId());

        assertThat(found).contains(unsaved);
    }

    @Test
    void shouldStream() {
        final var unsavedA = IngredientRecord.raw("MILK");
        final var unsavedB = IngredientRecord.raw("SALT");
        repository.saveAll(List.of(unsavedA, unsavedB));

        try (final var found = repository.readAll()) {
            assertThat(found).containsExactly(unsavedA, unsavedB);
        }
    }
}
