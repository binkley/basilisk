package hm.binkley.basilisk.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class StandardRepositoryTest {
    private static final String CODE = "ABC";
    private static final int NUMBER = 2;

    @Mock
    private final MyTestRepository repository;

    @Test
    void shouldReadAll() {
        final var saved = new MyTestRecord(1L, EPOCH, CODE, NUMBER);
        when(repository.findAll()).thenReturn(
                List.of(saved));
        when(repository.readAll()).thenCallRealMethod();

        final var found = repository.readAll();

        assertThat(found).containsExactly(saved);
    }

    @Test
    void shouldInsertWhenNew() {
        final var update = MyTestRecord.unsaved(CODE, NUMBER);
        final BiConsumer<MyTestRecord, @NotNull MyTestRecord> prepare
                = this::replaceWith;
        when(repository.upsert(update, prepare))
                .thenCallRealMethod();
        when(repository.findByCode(update.code))
                .thenReturn(Optional.empty());
        final var saved = new MyTestRecord(1L, EPOCH, update.code, NUMBER);
        when(repository.save(update))
                .thenReturn(saved);

        final var upserted = repository.upsert(update, prepare);

        assertThat(upserted).isEqualTo(saved);
    }

    @Test
    void shouldUpdateWhenExisting() {
        final var update = MyTestRecord.unsaved(CODE, NUMBER);
        final BiConsumer<MyTestRecord, @NotNull MyTestRecord> prepare
                = this::replaceWith;
        final var found = new MyTestRecord(1L, EPOCH, update.code, 1);
        when(repository.upsert(update, prepare))
                .thenCallRealMethod();
        when(repository.findByCode(update.code))
                .thenReturn(Optional.of(found));
        when(repository.save(update))
                .thenReturn(update);

        final var upserted = repository.upsert(update, prepare);

        assertThat(upserted).isEqualTo(found);
    }

    private void replaceWith(final MyTestRecord found,
            final @NotNull MyTestRecord update) {
        // TODO: Reconcile with upsert
        if (null == found) return;
        update.id = found.id;
        update.receivedAt = found.receivedAt;
    }
}
