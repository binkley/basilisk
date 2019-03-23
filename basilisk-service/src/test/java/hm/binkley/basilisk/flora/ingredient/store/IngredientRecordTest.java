package hm.binkley.basilisk.flora.ingredient.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hm.binkley.basilisk.flora.FloraFixtures.savedUnusedIngredientRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedUnusedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class IngredientRecordTest {
    @Mock
    private final IngredientStore store;

    @Test
    void shouldSave() {
        final var unsaved = unsavedUnusedIngredientRecord();
        unsaved.store = store;
        final var saved = savedUnusedIngredientRecord();
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
