package hm.binkley.basilisk.flora.chef.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static hm.binkley.basilisk.flora.FloraFixtures.savedChefRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedChefRecord;
import static org.assertj.core.api.Assertions.assertThat;
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
        final var record = unsavedChefRecord();

        final var unsaved = store.unsaved(record.getCode(), record.getName());

        assertThat(unsaved).isEqualTo(record);
        assertThat(unsaved.store).isSameAs(store);

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
}
