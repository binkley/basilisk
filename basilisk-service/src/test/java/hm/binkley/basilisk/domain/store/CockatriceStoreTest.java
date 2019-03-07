package hm.binkley.basilisk.domain.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.math.BigDecimal.TEN;
import static java.time.Instant.EPOCH;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CockatriceStoreTest {
    @Mock
    private CockatriceRepository springData;

    private CockatriceStore store;

    @BeforeEach
    void setUp() {
        store = new CockatriceStore(springData);
    }

    @Test
    void shouldFindById() {
        final var saved = new CockatriceRecord(3L, EPOCH, TEN);
        when(springData.findById(saved.getId()))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(saved.getId());

        assertThat(found).contains(saved);
        assertThat(found.orElseThrow().store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = new CockatriceRecord(3L, EPOCH, TEN);
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all().collect(toList());

        assertThat(found).containsExactly(saved);
        assertThat(found.stream().map(it -> it.store).collect(toList()))
                .containsExactly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldCreate() {
        final var unsaved = CockatriceRecord.raw(TEN);
        final var saved = new CockatriceRecord(3L, EPOCH,
                unsaved.getBeakSize());
        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.create()).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = CockatriceRecord.raw(TEN);
        final var saved = new CockatriceRecord(3L, EPOCH,
                unsaved.getBeakSize());

        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
