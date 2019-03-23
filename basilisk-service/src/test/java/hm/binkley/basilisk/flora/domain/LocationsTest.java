package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.LocationRecord;
import hm.binkley.basilisk.flora.domain.store.LocationStore;
import hm.binkley.basilisk.flora.rest.LocationRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedLocationRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class LocationsTest {
    @Mock
    private final LocationStore store;

    private Locations locations;

    @BeforeEach
    void setUp() {
        locations = new Locations(store);
    }

    @Test
    void shouldFindById() {
        final var record = savedLocationRecord();
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Location> found = locations.byId(record.getId());

        assertThat(found).contains(new Location(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByRef() {
        final var record = savedLocationRecord();
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Location> found = locations.byRef(record.ref());

        assertThat(found).contains(new Location(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByCode() {
        final var record = savedLocationRecord();
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = locations.byCode(record.getCode()).orElseThrow();

        assertThat(found).isEqualTo(new Location(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = savedLocationRecord();
        when(store.byName(record.getName()))
                .thenReturn(Optional.of(record));

        final var found = locations.byName(record.getName()).orElseThrow();

        assertThat(found).isEqualTo(new Location(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = savedLocationRecord();
        when(store.all())
                .thenReturn(Stream.of(record));

        final Stream<Location> found = locations.all();

        assertThat(found).containsExactly(new Location(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreate() {
        final var record = savedLocationRecord();
        when(store.save(LocationRecord.unsaved(
                record.getCode(), record.getName())))
                .thenReturn(record);

        assertThat(locations.create(LocationRequest.builder()
                .code(record.getCode())
                .name(record.getName())
                .build()))
                .isEqualTo(new Location(record));

        verifyNoMoreInteractions(store);
    }
}
