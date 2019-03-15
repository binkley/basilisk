package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChefRecordTest {
    @Mock
    private ChefStore store;

    @Test
    void shouldSave() {
        final var unsaved = ChefRecord.raw("Chef Boy-ar-dee");
        unsaved.store = store;
        final var saved = new ChefRecord(3L, EPOCH, unsaved.getName());
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldClone() {
        final var unsaved = new ChefRecord(null, EPOCH, "Chef Boy-ar-dee");

        assertThat(unsaved.clone()).isEqualTo(unsaved);
    }
}
