package hm.binkley.basilisk.flora.location.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hm.binkley.basilisk.flora.FloraFixtures.savedLocationRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedLocationRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class LocationRecordTest {
    @Mock
    private final LocationStore store;

    @Test
    void shouldSave() {
        final var unsaved = unsavedLocationRecord();
        unsaved.store = store;
        final var saved = savedLocationRecord();
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldClone() {
        final var saved = savedLocationRecord();

        assertThat(saved.clone()).isEqualTo(saved);
    }
}
