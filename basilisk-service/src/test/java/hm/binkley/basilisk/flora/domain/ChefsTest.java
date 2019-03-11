package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.ChefRecord;
import hm.binkley.basilisk.flora.domain.store.ChefStore;
import hm.binkley.basilisk.flora.rest.ChefRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChefsTest {
    @Mock
    private ChefStore store;

    private Chefs recipes;

    @BeforeEach
    void setUp() {
        recipes = new Chefs(store);
    }

    @Test
    void shouldFindById() {
        final var record = new ChefRecord(
                3L, EPOCH, "ABC", "Chef Paul");
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Chef> found = recipes.byId(record.getId());

        assertThat(found).contains(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByCode() {
        final var record = new ChefRecord(
                3L, EPOCH, "DEF", "Chef Mary");
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = recipes.byCode(record.getCode()).orElseThrow();

        assertThat(found).isEqualTo(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = new ChefRecord(
                3L, EPOCH, "GHI", "Chef Robert");
        when(store.byName(record.getName()))
                .thenReturn(Optional.of(record));

        final var found = recipes.byName(record.getName()).orElseThrow();

        assertThat(found).isEqualTo(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = new ChefRecord(
                3L, EPOCH, "JKL", "Chef Josephine");
        when(store.all())
                .thenReturn(Stream.of(record));

        final Stream<Chef> found = recipes.all();

        assertThat(found).containsOnly(new Chef(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreate() {
        final var record = new ChefRecord(
                3L, EPOCH, "MNO", "Chef Howard");

        when(store.save(ChefRecord.raw(record.getCode(), record.getName())))
                .thenReturn(record);

        assertThat(recipes.create(ChefRequest.builder()
                .code(record.getCode())
                .name(record.getName())
                .build()))
                .isEqualTo(new Chef(record));

        verifyNoMoreInteractions(store);
    }
}
