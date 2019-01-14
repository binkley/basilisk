package hm.binkley.basilisk.database;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
class BasiliskRepositoryTest {
    @Autowired
    private BasiliskRepository repository;

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
}
