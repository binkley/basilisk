package hm.binkley.basilisk.domain.store;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static java.math.BigDecimal.TEN;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class BasiliskRepositoryTest {
    private final BasiliskRepository repository;

    @Test
    void shouldRoundtrip() {
        final var unsaved = BasiliskRecord.raw("BIRD",
                Instant.ofEpochSecond(1_000_000));
        final var found = repository.findById(
                repository.save(unsaved).getId());

        assertThat(found).contains(unsaved);
    }

    @Test
    void shouldRoundtripWithCockatrice() {
        final var unsaved = BasiliskRecord.raw("BIRD",
                Instant.ofEpochSecond(1_000_000))
                .add(CockatriceRecord.raw(TEN));
        final var found = repository.findById(
                repository.save(unsaved).getId());

        assertThat(found).contains(unsaved);
    }

    @Test
    void shouldFindByWord() {
        final var unsavedLeft = BasiliskRecord.raw("LEFT",
                Instant.ofEpochSecond(1_000_000));
        final var unsavedRight = BasiliskRecord.raw("RIGHT",
                Instant.ofEpochSecond(1_000_000));
        repository.saveAll(List.of(unsavedLeft, unsavedRight));

        assertThat(repository.findByWord("LEFT").collect(toList()))
                .isEqualTo(List.of(unsavedLeft));
        assertThat(repository.findByWord("MIDDLE").collect(toList()))
                .isEmpty();
    }

    @Test
    void shouldStream() {
        final var unsavedA = BasiliskRecord.raw("BIRD",
                Instant.ofEpochSecond(1_000_000));
        final var unsavedB = BasiliskRecord.raw("WORD",
                Instant.ofEpochSecond(1_000_000));
        repository.saveAll(List.of(unsavedA, unsavedB));

        // Wrap in try-with-resources to close the stream at done; this
        // frees up DB resources as a stream is potentially very long
        try (final var found = repository.readAll()) {
            assertThat(found).containsExactly(unsavedA, unsavedB);
        }
    }
}
