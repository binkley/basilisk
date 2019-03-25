package hm.binkley.basilisk.flora.source.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedSourceRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class SourceStoreTest {
    @Mock
    private final SourceRepository springData;

    private SourceStore store;

    @BeforeEach
    void setUp() {
        store = new SourceStore(springData);
    }

    @Test
    void shouldCreateUnsaved() {
        final var record = unsavedSourceRecord();

        final var unsaved = store.unsaved(record.getCode(), record.getName());

        assertThat(unsaved).isEqualTo(record);
        assertThat(unsaved.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var saved = savedSourceRecord();
        when(springData.findByName(SOURCE_NAME))
                .thenReturn(Optional.of(saved));

        final var found = store.byName(SOURCE_NAME).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }
}
