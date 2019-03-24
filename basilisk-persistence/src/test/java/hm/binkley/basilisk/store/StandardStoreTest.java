package hm.binkley.basilisk.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class StandardStoreTest {
    @Mock
    private final MyTestRepository repository;

    private MyTestStore store;
    private MyTestRecord unsaved;
    private MyTestRecord saved;

    @BeforeEach
    void setUp() {
        store = new MyTestStore(repository);
        unsaved = store.unsaved("ABC", 2);
        saved = store.unsaved("ABC", 2);
        saved.id = 1L;
        saved.receivedAt = EPOCH;
    }

    @Test
    void shouldFindById() {
        when(repository.findById(saved.getId()))
                .thenReturn(Optional.of(saved));

        assertThat(store.byId(saved.getId()).orElseThrow())
                .isEqualTo(saved);
    }

    @Test
    void shouldFindByCode() {
        when(repository.findByCode(saved.getCode()))
                .thenReturn(Optional.of(saved));

        assertThat(store.byCode(saved.getCode()).orElseThrow())
                .isEqualTo(saved);
    }

    @Test
    void shouldFindAll() {
        when(repository.findAll())
                .thenReturn(List.of(saved));
        when(repository.readAll()).thenCallRealMethod();

        assertThat(store.all()).containsExactly(saved);
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

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @Test
    void shouldResave() {
        final var firstId = 1L;
        final var secondId = 2L;
        when(repository.save(unsaved)).thenAnswer(invocation -> {
            final MyTestRecord record = invocation.getArgument(0);
            record.id = firstId;
            return record;
        }).thenAnswer(invocation -> {
            final MyTestRecord record = invocation.getArgument(0);
            record.id = secondId;
            return record;
        });

        final var saved = store.save(unsaved);
        assertThat(saved.id).isEqualTo(firstId);
        assertThat(unsaved.id).isEqualTo(firstId);

        final var deleted = store.delete(saved);
        assertThat(deleted.id).isNull();
        assertThat(saved.id).isNull();

        final var resaved = store.save(deleted);
        assertThat(resaved.id).isEqualTo(secondId);
        assertThat(deleted.id).isEqualTo(secondId);
    }

    @Test
    void shouldComplainOnMissingId() {
        assertThatThrownBy(() -> store.byId(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnMissingCode() {
        assertThatThrownBy(() -> store.byCode(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnMissingRecord() {
        assertThatThrownBy(() -> store.save(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> store.delete(null))
                .isInstanceOf(NullPointerException.class);
    }
}
