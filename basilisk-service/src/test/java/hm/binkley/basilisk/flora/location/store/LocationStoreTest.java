package hm.binkley.basilisk.flora.location.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static hm.binkley.basilisk.flora.FloraFixtures.savedLocationRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedLocationRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class LocationStoreTest {
    @Mock
    private final LocationRepository springData;

    private LocationStore store;

    @BeforeEach
    void setUp() {
        store = new LocationStore(springData);
    }

    @Test
    void shouldCreateUnsaved() {
        final var record = unsavedLocationRecord();

        final var unsaved = store.unsaved(record.getCode(), record.getName());

        assertThat(unsaved).isEqualTo(record);
        assertThat(unsaved.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var saved = savedLocationRecord();
        when(springData.findByName(saved.getName()))
                .thenReturn(Optional.of(saved));

        final var found = store.byName(saved.getName()).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }
}
