package hm.binkley.basilisk.basilisk.domain;

import hm.binkley.basilisk.basilisk.domain.store.BasiliskRecord;
import hm.binkley.basilisk.basilisk.domain.store.BasiliskStore;
import hm.binkley.basilisk.basilisk.domain.store.CockatriceRecord;
import hm.binkley.basilisk.basilisk.rest.BasiliskRequest;
import hm.binkley.basilisk.basilisk.rest.CockatriceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.math.BigDecimal.TEN;
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

    @Test
    void shouldCreate() {
        final var word = "QUX";
        final var at = Instant.ofEpochSecond(1L);
        final var beakSize = TEN;
        final CockatriceRecord cockatriceRecord = new CockatriceRecord(
                5L, EPOCH.plusSeconds(11L), beakSize);
        final var record = new BasiliskRecord(3L, EPOCH, word, at)
                .add(cockatriceRecord);

        when(store.save(BasiliskRecord.raw(record.getWord(), record.getAt())
                .add(CockatriceRecord.raw(cockatriceRecord.getBeakSize()))))
                .thenReturn(record);

        assertThat(basilisks.create(BasiliskRequest.builder()
                .word(word)
                .at(at)
                .cockatrices(List.of(CockatriceRequest.builder()
                        .beakSize(beakSize)
                        .build()))
                .build()))
                .isEqualTo(new Basilisk(record));

        verifyNoMoreInteractions(store);
    }
}
