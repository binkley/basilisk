package hm.binkley.basilisk.store;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJdbcTest
class BasiliskRepositoryTest {
    @Autowired
    private BasiliskRepository basilisks;

    @SuppressFBWarnings("NP")
    @Test
    void shouldRoundtrip() {
        final var unsaved = BasiliskRecord.builder()
                .word("BIRD")
                .when(Instant.ofEpochSecond(1_000_000))
                .build();
        final var found = basilisks.findById(
                basilisks.save(unsaved).getId());

        assertThat(found).contains(unsaved);
    }

    @Test
    void shouldStream() {
        final var unsavedA = BasiliskRecord.builder()
                .word("BIRD")
                .when(Instant.ofEpochSecond(1_000_000))
                .build();
        final var unsavedB = BasiliskRecord.builder()
                .word("WORD")
                .when(Instant.ofEpochSecond(1_000_000))
                .build();
        basilisks.saveAll(List.of(unsavedA, unsavedB));

        // Wrap in try-with-resources to close the stream when done; this
        // frees up DB resources as a stream is potentially very long
        try (final var found = basilisks.readAll()) {
            assertThat(found).containsExactly(unsavedA, unsavedB);
        }
    }
}
