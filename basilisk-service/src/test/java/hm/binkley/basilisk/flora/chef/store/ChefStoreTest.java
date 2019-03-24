package hm.binkley.basilisk.flora.chef.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_RECIEVED_AT;
import static hm.binkley.basilisk.flora.FloraFixtures.savedChefRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedChefRecord;
import static hm.binkley.basilisk.store.PersistenceTesting.simulateRepositoryDelete;
import static hm.binkley.basilisk.store.PersistenceTesting.simulateRepositorySave;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class ChefStoreTest {
    @Mock
    private final ChefRepository springData;

    private ChefStore store;

    @BeforeEach
    void setUp() {
        store = new ChefStore(springData);
    }

    @Test
    void shouldCreateUnsaved() {
        final var unsaved = unsavedChefRecord();

        assertThat(store.unsaved(unsaved.getCode(), unsaved.getName()))
                .isEqualTo(unsaved);
    }

    @Test
    void shouldFindById() {
        final var saved = savedChefRecord();
        when(springData.findById(saved.getId()))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(saved.getId()).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var saved = savedChefRecord();
        when(springData.findByName(saved.getName()))
                .thenReturn(Optional.of(saved));

        final var found = store.byName(saved.getName()).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = savedChefRecord();
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all().collect(toSet());

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = unsavedChefRecord();
        when(springData.save(unsaved))
                .then(simulateRepositorySave(CHEF_ID, CHEF_RECIEVED_AT));

        final var saved = store.save(unsaved);

        assertThat(saved).isEqualTo(unsaved);
        assertThat(saved.getId()).isEqualTo(CHEF_ID);
        assertThat(saved.getReceivedAt()).isEqualTo(CHEF_RECIEVED_AT);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldDelete() {
        final var saved = savedChefRecord();
        doAnswer(simulateRepositoryDelete())
                .when(springData).delete(saved);

        final var deleted = store.delete(saved);

        assertThat(deleted).isEqualTo(saved);
        assertThat(deleted.getId()).isNull();
        assertThat(deleted.getReceivedAt()).isNull();

        verify(springData).delete(saved);
        verifyNoMoreInteractions(springData);
    }
}
