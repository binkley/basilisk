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
        final var unsaved = BasiliskRepository.BasiliskRecord.builder()
                .word("BIRD")
                .at(Instant.ofEpochSecond(1_000_000))
                .build();
        final var found = basilisks.findById(
                basilisks.save(unsaved).getId());

        assertThat(found).contains(unsaved);
    }

    @Test
    void shouldFindByWord() {
        final var unsavedLeft = BasiliskRepository.BasiliskRecord.builder()
                .word("LEFT")
                .at(Instant.ofEpochSecond(1_000_000))
                .build();
        final var unsavedRight = BasiliskRepository.BasiliskRecord.builder()
                .word("RIGHT")
                .at(Instant.ofEpochSecond(1_000_000))
                .build();
        basilisks.saveAll(List.of(unsavedLeft, unsavedRight));

        assertThat(basilisks.findByWord("LEFT"))
                .isEqualTo(List.of(unsavedLeft));
        assertThat(basilisks.findByWord("MIDDLE")).isEmpty();
    }

    @Test
    void shouldStream() {
        final var unsavedA = BasiliskRepository.BasiliskRecord.builder()
                .word("BIRD")
                .at(Instant.ofEpochSecond(1_000_000))
                .build();
        final var unsavedB = BasiliskRepository.BasiliskRecord.builder()
                .word("WORD")
                .at(Instant.ofEpochSecond(1_000_000))
                .build();
        basilisks.saveAll(List.of(unsavedA, unsavedB));

        // Wrap in try-with-resources to close the stream at done; this
        // frees up DB resources as a stream is potentially very long
        try (final var found = basilisks.readAll()) {
            assertThat(found).containsExactly(unsavedA, unsavedB);
        }
    }
}
