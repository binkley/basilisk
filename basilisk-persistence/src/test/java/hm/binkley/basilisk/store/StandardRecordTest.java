package hm.binkley.basilisk.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicReference;

import static hm.binkley.basilisk.store.PersistenceTesting.simulateRepositorySave;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class StandardRecordTest {
    @Mock
    private final MyTestStore store;

    private MyTestRecord record;

    @BeforeEach
    void setUp() {
        record = MyTestRecord.unsaved("ABC", 123);
        record.store = store;
    }

    @Test
    void shouldNotSaveOnRef() {
        final Long savedId = 1L;
        record.id = savedId;
        record.receivedAt = EPOCH;

        final var ref = record.asRef(AtomicReference::new);

        assertThat(ref.get()).isEqualTo(savedId);

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldSaveOnRef() {
        final Long savedId = 1L;
        when(store.save(record))
                .then(simulateRepositorySave(savedId, EPOCH));

        final var ref = record.asRef(AtomicReference::new);

        assertThat(ref.get()).isEqualTo(savedId);

        verify(store).save(record);
        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldSave() {
        record.save();

        verify(store).save(record);
    }

    @Test
    void shouldDelete() {
        record.delete();

        verify(store).delete(record);
    }

    @Test
    void shouldSort() {
        final var recordA = MyTestRecord.unsaved("ABC", 2);
        final var recordB = MyTestRecord.unsaved("DEF", 1);

        assertThat(recordA).isLessThan(recordB);
    }
}
