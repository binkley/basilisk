package hm.binkley.basilisk.domain.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static java.time.Instant.EPOCH;
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
        final BasiliskRecord saved = new BasiliskRecord(
                3L, EPOCH, "FOO", Instant.ofEpochSecond(1L));
        when(springData.findById(3L))
                .thenReturn(Optional.of(saved));

        final BasiliskRecord found = store.byId(3L).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);
    }

    @Test
    void shouldSave() {
        final BasiliskRecord unsaved = new BasiliskRecord(
                null, null, "FOO", Instant.ofEpochSecond(1L));
        final BasiliskRecord saved = new BasiliskRecord(
                3L, EPOCH, "FOO", Instant.ofEpochSecond(1L));

        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
