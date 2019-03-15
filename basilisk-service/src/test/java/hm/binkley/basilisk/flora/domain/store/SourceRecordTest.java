package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_NAME;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SourceRecordTest {
    @Mock
    private SourceStore store;

    @Test
    void shouldSave() {
        final var unsaved = SourceRecord.raw(INGREDIENT_NAME);
        unsaved.store = store;
        final var saved = new SourceRecord(3L, EPOCH, unsaved.getName());
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldClone() {
        final var unsaved = new SourceRecord(null, null, INGREDIENT_NAME);

        assertThat(unsaved.clone()).isEqualTo(unsaved);
    }
}
