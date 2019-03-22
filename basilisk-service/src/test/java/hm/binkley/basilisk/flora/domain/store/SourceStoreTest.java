package hm.binkley.basilisk.flora.domain.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_NAME;
import static java.time.Instant.EPOCH;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
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
    void shouldFindById() {
        final var saved = new SourceRecord(SOURCE_ID, EPOCH, SOURCE_NAME);
        when(springData.findById(SOURCE_ID))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(SOURCE_ID).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var saved = new SourceRecord(SOURCE_ID, EPOCH, SOURCE_NAME);
        when(springData.findByName(SOURCE_NAME))
                .thenReturn(Optional.of(saved));

        final var found = store.byName(SOURCE_NAME).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = new SourceRecord(3L, EPOCH, "MILK");
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all().collect(toSet());

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldCreate() {
        final var unsaved = SourceRecord.unsaved("SALT");
        final var saved = new SourceRecord(
                SOURCE_ID, EPOCH, unsaved.getName());
        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.create(unsaved.getName()))
                .isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = SourceRecord.unsaved("PEPPER");
        final var saved = new SourceRecord(
                SOURCE_ID, EPOCH, unsaved.getName());

        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
