package hm.binkley.basilisk.domain.store;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class CockatriceRepositoryTest {
    private final CockatriceRepository repository;

    @Test
    void shouldRoundtrip() {
        final var unsaved = CockatriceRecord.raw(TEN);
        final var found = repository.findById(
                repository.save(unsaved).getId());

        assertThat(found).contains(unsaved);
    }

    @Test
    void shouldStream() {
        final var unsavedA = CockatriceRecord.raw(TEN);
        final var unsavedB = CockatriceRecord.raw(TEN);
        repository.saveAll(List.of(unsavedA, unsavedB));

        try (final var found = repository.readAll()) {
            assertThat(found).containsExactly(unsavedA, unsavedB);
        }
    }
}
