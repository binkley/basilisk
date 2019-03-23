package hm.binkley.basilisk.flora.recipe.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.RECIPE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.savedRecipeRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedRecipeRecord;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class RecipeStoreTest {
    @Mock
    private final RecipeRepository springData;

    private RecipeStore store;

    @BeforeEach
    void setUp() {
        store = new RecipeStore(springData);
    }

    @Test
    void shouldFindById() {
        final var saved = savedRecipeRecord();
        when(springData.findById(saved.getId()))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(saved.getId()).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var saved = savedRecipeRecord();
        when(springData.findByName(saved.getName()))
                .thenReturn(Optional.of(saved));

        final var found = store.byName(saved.getName()).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = savedRecipeRecord();
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all().collect(toSet());

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldCreate() {
        final var unsaved = unsavedRecipeRecord();
        final var saved = savedRecipeRecord();
        when(springData.save(unsaved))
                .thenReturn(saved);
        when(springData.findById(RECIPE_ID)) // TODO: See RecipeRepositoryTest
                .thenReturn(Optional.of(saved));

        assertThat(store.create(
                unsaved.getCode(), unsaved.getName(), unsaved.getChefId()))
                .isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = unsavedRecipeRecord();
        final var saved = savedRecipeRecord();

        when(springData.save(unsaved))
                .thenReturn(saved);
        // TODO: See RecipeRepositoryTest
        when(springData.findById(saved.getId()))
                .thenReturn(Optional.of(saved));

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
