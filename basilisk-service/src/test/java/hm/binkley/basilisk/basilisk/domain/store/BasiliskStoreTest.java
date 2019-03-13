package hm.binkley.basilisk.basilisk.domain.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.time.Instant.EPOCH;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasiliskStoreTest {
    @Mock
    private BasiliskRepository springData;

    private BasiliskStore store;

    @BeforeEach
    void setUp() {
        store = new BasiliskStore(springData);
    }

    @Test
    void shouldFindById() {
        final var saved = new BasiliskRecord(
                3L, EPOCH, "FOO", Instant.ofEpochSecond(1L));
        when(springData.findById(saved.getId()))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(saved.getId()).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByWord() {
        final var saved = new BasiliskRecord(
                3L, EPOCH, "BAR", Instant.ofEpochSecond(1L));
        when(springData.findByWord(saved.getWord()))
                .thenReturn(Stream.of(saved));

        final var found = store.byWord(saved.getWord()).collect(toSet());

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = new BasiliskRecord(
                3L, EPOCH, "BAZ", Instant.ofEpochSecond(1L));
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all().collect(toSet());

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldCreate() {
        final var word = "QUZ";
        final var at = Instant.ofEpochSecond(1L);
        final var unsaved = BasiliskRecord.raw(word, at);
        final var saved = new BasiliskRecord(
                3L, EPOCH, unsaved.getWord(), unsaved.getAt());
        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.create(word, at)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = BasiliskRecord.raw(
                "QUUX", Instant.ofEpochSecond(1L));
        final var saved = new BasiliskRecord(
                3L, EPOCH, unsaved.getWord(), unsaved.getAt());

        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
