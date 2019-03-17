package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.SourceRecord;
import hm.binkley.basilisk.flora.domain.store.SourceStore;
import hm.binkley.basilisk.flora.rest.SourceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_NAME;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SourcesTest {
    @Mock
    private SourceStore store;

    private Sources sources;

    @BeforeEach
    void setUp() {
        sources = new Sources(store);
    }

    @Test
    void shouldFindById() {
        final var record = new SourceRecord(
                SOURCE_ID, EPOCH, SOURCE_NAME);
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Source> found = sources.byId(record.getId());

        assertThat(found).contains(new Source(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = new SourceRecord(
                SOURCE_ID, EPOCH, SOURCE_NAME);
        when(store.byName(record.getName()))
                .thenReturn(Optional.of(record));

        final var found = sources.byName(record.getName()).orElseThrow();

        assertThat(found).isEqualTo(new Source(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = new SourceRecord(
                SOURCE_ID, EPOCH, SOURCE_NAME);
        when(store.all())
                .thenReturn(Stream.of(record));

        final Stream<Source> found = sources.all();

        assertThat(found).containsExactly(new Source(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreateNew() {
        final var record = new SourceRecord(
                SOURCE_ID, EPOCH, SOURCE_NAME);

        when(store.save(SourceRecord.raw(record.getName())))
                .thenReturn(record);

        assertThat(sources.create(SourceRequest.builder()
                .name(record.getName())
                .build()))
                .isEqualTo(new Source(record));

        verifyNoMoreInteractions(store);
    }
}
