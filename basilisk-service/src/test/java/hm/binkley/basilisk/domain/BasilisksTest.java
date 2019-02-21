package hm.binkley.basilisk.domain;

import hm.binkley.basilisk.domain.store.BasiliskRecord;
import hm.binkley.basilisk.domain.store.BasiliskStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasilisksTest {
    @Mock
    private BasiliskStore store;

    private Basilisks basilisks;

    @BeforeEach
    void setUp() {
        basilisks = new Basilisks(store);
    }

    @Test
    void shouldFindById() {
        final var record = new BasiliskRecord(
                3L, EPOCH, "FOO", Instant.ofEpochSecond(1L));
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Basilisk> found = basilisks.byId(record.getId());

        assertThat(found).contains(new Basilisk(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByWord() {
        final var record = new BasiliskRecord(
                3L, EPOCH, "BAR", Instant.ofEpochSecond(1L));
        when(store.byWord(record.getWord()))
                .thenReturn(Stream.of(record));

        final Stream<Basilisk> found = basilisks.byWord(record.getWord());

        assertThat(found).containsExactly(new Basilisk(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = new BasiliskRecord(
                3L, EPOCH, "BAZ", Instant.ofEpochSecond(1L));
        when(store.all())
                .thenReturn(Stream.of(record));

        final Stream<Basilisk> found = basilisks.all();

        assertThat(found).containsExactly(new Basilisk(record));

        verifyNoMoreInteractions(store);
    }
}
