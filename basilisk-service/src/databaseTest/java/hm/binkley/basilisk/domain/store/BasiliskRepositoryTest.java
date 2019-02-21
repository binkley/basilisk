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

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class BasiliskRepositoryTest {
    private final BasiliskRepository basilisks;

    @Test
    void shouldRoundtrip() {
        final var unsaved = BasiliskRecord.createRaw("BIRD",
                Instant.ofEpochSecond(1_000_000));
        final var found = basilisks.findById(
                basilisks.save(unsaved).getId());

        assertThat(found).contains(unsaved);
    }

    @Test
    void shouldFindByWord() {
        final var unsavedLeft = BasiliskRecord.createRaw("LEFT",
                Instant.ofEpochSecond(1_000_000));
        final var unsavedRight = BasiliskRecord.createRaw("RIGHT",
                Instant.ofEpochSecond(1_000_000));
        basilisks.saveAll(List.of(unsavedLeft, unsavedRight));

        assertThat(basilisks.findByWord("LEFT").collect(toList()))
                .isEqualTo(List.of(unsavedLeft));
        assertThat(basilisks.findByWord("MIDDLE").collect(toList()))
                .isEmpty();
    }

    @Test
    void shouldStream() {
        final var unsavedA = BasiliskRecord.createRaw("BIRD",
                Instant.ofEpochSecond(1_000_000));
        final var unsavedB = BasiliskRecord.createRaw("WORD",
                Instant.ofEpochSecond(1_000_000));
        basilisks.saveAll(List.of(unsavedA, unsavedB));

        // Wrap in try-with-resources to close the stream at done; this
        // frees up DB resources as a stream is potentially very long
        try (final var found = basilisks.readAll()) {
            assertThat(found).containsExactly(unsavedA, unsavedB);
        }
    }
}
