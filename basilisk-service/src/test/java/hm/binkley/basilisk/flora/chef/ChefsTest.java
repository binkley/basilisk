package hm.binkley.basilisk.flora.chef;

import hm.binkley.basilisk.flora.chef.store.ChefStore;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.savedChefRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedChefRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class ChefsTest {
    @Mock
    private final ChefStore store;

    private Chefs chefs;

    @BeforeEach
    void setUp() {
        chefs = new Chefs(store);
    }

    @Test
    void shouldCreateUnsaved() {
        final var unsaved = unsavedChefRecord();
        when(store.unsaved(unsaved.getCode(), unsaved.getName()))
                .thenReturn(unsaved);

        assertThat(chefs.unsaved(unsaved.getCode(), unsaved.getName()))
                .isEqualTo(new Chef(unsaved));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindById() {
        final var record = savedChefRecord();
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final var found = chefs
                .byId(record.getId())
                .orElseThrow();

        assertThat(found).isEqualTo(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByCode() {
        final var record = savedChefRecord();
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = chefs
                .byCode(record.getCode())
                .orElseThrow();

        assertThat(found).isEqualTo(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = savedChefRecord();
        when(store.byName(record.getName()))
                .thenReturn(Optional.of(record));

        final var found = chefs
                .byName(record.getName())
                .orElseThrow();

        assertThat(found).isEqualTo(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = savedChefRecord();
        when(store.all())
                .thenReturn(Stream.of(record));

        final var found = chefs.all();

        assertThat(found).containsExactly(new Chef(record));

        verifyNoMoreInteractions(store);
    }
}
