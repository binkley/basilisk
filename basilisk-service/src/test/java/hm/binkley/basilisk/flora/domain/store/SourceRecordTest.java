package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedSourceRecord;
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
        final var unsaved = unsavedSourceRecord();
        unsaved.store = store;
        final var saved = savedSourceRecord();
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldClone() {
        final var saved = savedSourceRecord();

        assertThat(saved.clone()).isEqualTo(saved);
    }
}
