package hm.binkley.basilisk.store;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
class BasiliskRepositoryTest {
    @Autowired
    private BasiliskRepository repository;

    @SuppressFBWarnings("NP")
    @Test
    void shouldRoundtrip() {
        final BasiliskRecord unsaved = BasiliskRecord.builder()
                .word("BIRD")
                .when(Instant.ofEpochSecond(1_000_000))
                .build();
        final Optional<BasiliskRecord> found = repository
                .findById(repository.save(unsaved).getId());

        assertThat(found).contains(unsaved);
    }

    @Test
    void shouldStream() {
        final BasiliskRecord unsavedA = BasiliskRecord.builder()
                .word("BIRD")
                .when(Instant.ofEpochSecond(1_000_000))
                .build();
        final BasiliskRecord unsavedB = BasiliskRecord.builder()
                .word("WORD")
                .when(Instant.ofEpochSecond(1_000_000))
                .build();
        repository.saveAll(List.of(unsavedA, unsavedB));

        // Wrap in try-with-resources to close the stream when done; this
        // frees up DB resources as a stream is potentially very long
        try (final Stream<BasiliskRecord> found = repository.readAll()) {
            assertThat(found).containsExactly(unsavedA, unsavedB);
        }
    }
}
