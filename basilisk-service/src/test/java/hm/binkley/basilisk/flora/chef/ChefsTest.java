package hm.binkley.basilisk.flora.chef;

import hm.binkley.basilisk.flora.chef.store.ChefRecord;
import hm.binkley.basilisk.flora.chef.store.ChefStore;
import hm.binkley.basilisk.flora.chef.rest.ChefRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.savedChefRecord;
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
    void shouldFindById() {
        final var record = savedChefRecord();
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Chef> found = chefs.byId(record.getId());

        assertThat(found).contains(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByCode() {
        final var record = savedChefRecord();
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = chefs.byCode(record.getCode()).orElseThrow();

        assertThat(found).isEqualTo(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = savedChefRecord();
        when(store.byName(record.getName()))
                .thenReturn(Optional.of(record));

        final var found = chefs.byName(record.getName()).orElseThrow();

        assertThat(found).isEqualTo(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = savedChefRecord();
        when(store.all())
                .thenReturn(Stream.of(record));

        final Stream<Chef> found = chefs.all();

        assertThat(found).containsExactly(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreate() {
        final var record = savedChefRecord();
        when(store.save(ChefRecord.unsaved(
                record.getCode(), record.getName())))
                .thenReturn(record);

        assertThat(chefs.create(ChefRequest.builder()
                .code(record.getCode())
                .name(record.getName())
                .build()))
                .isEqualTo(new Chef(record));

        verifyNoMoreInteractions(store);
    }
}
