package hm.binkley.basilisk.flora.ingredient.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.savedUsedIngredientRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedUnusedIngredientRecord;
import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class IngredientStoreTest {
    @Mock
    private final IngredientRepository springData;

    private IngredientStore store;

    @BeforeEach
    void setUp() {
        store = new IngredientStore(springData);
    }

    @Test
    void shouldCreateUnsaved() {
        final var record = unsavedUnusedIngredientRecord();

        final var unsaved = store.unsaved(
                record.getCode(), record.getSourceId(), record.getName(),
                record.getQuantity(), record.getChefId());

        assertThat(unsaved).isEqualTo(record);
        assertThat(unsaved.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindById() {
        final var id = 3L;
        final var saved = unsavedUnusedIngredientRecord();
        when(springData.findById(id))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(id).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var saved = savedUsedIngredientRecord();
        when(springData.findAllByName(saved.getName()))
                .thenReturn(Stream.of(saved));

        final var found = store.byName(saved.getName())
                .collect(toCollection(TreeSet::new));

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindUnused() {
        final var saved = savedUsedIngredientRecord();
        when(springData.findAllByRecipeIdIsNull()).
                thenReturn(Stream.of(saved));

        final var found = store.unused()
                .collect(toCollection(TreeSet::new));

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = savedUsedIngredientRecord();
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all()
                .collect(toCollection(TreeSet::new));

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldCreate() {
        final var unsaved = unsavedUnusedIngredientRecord();
        final var saved = savedUsedIngredientRecord();
        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.create(unsaved.getCode(), unsaved.getSourceId(),
                unsaved.getName(), unsaved.getQuantity(),
                unsaved.getChefId()))
                .isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = unsavedUnusedIngredientRecord();
        final var saved = savedUsedIngredientRecord();

        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
