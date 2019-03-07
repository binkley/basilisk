package hm.binkley.basilisk.basilisk.domain.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.math.BigDecimal.TEN;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CockatriceRecordTest {
    @Mock
    private CockatriceStore store;

    @Test
    void shouldSave() {
        final var unsaved = CockatriceRecord.raw(TEN);
        unsaved.store = store;
        final var saved = new CockatriceRecord(3L, EPOCH,
                unsaved.getBeakSize());
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }
}
