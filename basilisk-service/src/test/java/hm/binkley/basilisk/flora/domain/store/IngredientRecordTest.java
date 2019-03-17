package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedUnusedIngredientRecord;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientRecordTest {
    @Mock
    private IngredientStore store;

    @Test
    void shouldSave() {
        final var unsaved = unsavedUnusedIngredientRecord();
        unsaved.store = store;
        final var saved = new IngredientRecord(
                1L, EPOCH, unsaved.getSourceId(), unsaved.getName(),
                unsaved.getQuantity(), 2L, unsaved.getChefId());
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldClone() {
        final var unsaved = unsavedUnusedIngredientRecord();

        assertThat(unsaved.clone()).isEqualTo(unsaved);
    }
}
