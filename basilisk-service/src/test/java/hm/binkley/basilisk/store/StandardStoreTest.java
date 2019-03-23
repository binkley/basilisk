package hm.binkley.basilisk.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class StandardStoreTest {
    private static final MyTestRecord unsaved
            = MyTestRecord.unsaved("ABC", 2);
    private static final MyTestRecord saved = new MyTestRecord(1L, EPOCH,
            unsaved.getCode(), unsaved.getNumber());

    @Mock
    private final MyTestRepository repository;

    private MyTestStore store;

    @BeforeEach
    void setUp() {
        store = new MyTestStore(repository);
    }

    @Test
    void shouldFindByCode() {
        when(repository.findByCode(saved.getCode()))
                .thenReturn(Optional.of(saved));

        assertThat(store.byCode(saved.getCode()).orElseThrow())
                .isEqualTo(saved);
    }

    @Test
    void shouldFindById() {
        when(repository.findById(saved.getId()))
                .thenReturn(Optional.of(saved));

        assertThat(store.byId(saved.getId()).orElseThrow())
                .isEqualTo(saved);
    }

    @Test
    void shouldSave() {
        when(repository.save(unsaved))
                .thenReturn(saved);

        assertThat(store.save(unsaved)).isEqualTo(saved);
    }

    @Test
    void shouldDelete() {
        store.delete(saved);

        verify(repository).delete(saved);
    }
}
