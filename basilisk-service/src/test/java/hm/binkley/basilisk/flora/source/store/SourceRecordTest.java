package hm.binkley.basilisk.flora.source.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedSourceRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class SourceRecordTest {
    @Mock
    private final SourceStore store;

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
