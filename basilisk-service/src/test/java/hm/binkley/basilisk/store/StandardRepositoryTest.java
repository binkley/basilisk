package hm.binkley.basilisk.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

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
    void shouldInsertWhenNew() {
        final var update = MyTestRecord.unsaved(CODE, NUMBER);
        final Function<MyTestRecord, Optional<MyTestRecord>> findBy
                = this::matching;
        final BiConsumer<MyTestRecord, @NotNull MyTestRecord> prepareUpsert
                = this::replaceWith;
        when(repository.upsert(update, prepareUpsert))
                .thenCallRealMethod();
        when(repository.findByCode(update.code))
                .thenReturn(Optional.empty());
        final var saved = new MyTestRecord(1L, EPOCH, update.code, NUMBER);
        when(repository.save(update))
                .thenReturn(saved);

        final var upserted = repository.upsert(update, prepareUpsert);

        assertThat(upserted).isEqualTo(saved);
    }

    @Test
    void shouldUpdateWhenExisting() {
        final var update = MyTestRecord.unsaved(CODE, NUMBER);
        final Function<MyTestRecord, Optional<MyTestRecord>> findBy
                = this::matching;
        final BiConsumer<MyTestRecord, @NotNull MyTestRecord> prepareUpsert
                = this::replaceWith;
        final var found = new MyTestRecord(1L, EPOCH, update.code, 1);
        when(repository.upsert(update, prepareUpsert))
                .thenCallRealMethod();
        when(repository.findByCode(update.code))
                .thenReturn(Optional.of(found));
        when(repository.save(update))
                .thenReturn(update);

        final var upserted = repository.upsert(update, prepareUpsert);

        assertThat(upserted).isEqualTo(found);
    }

    private Optional<MyTestRecord> matching(final MyTestRecord maybeNew) {
        return repository.findByCode(maybeNew.code);
    }

    private void replaceWith(final MyTestRecord found,
            final @NotNull MyTestRecord update) {
        // TODO: Reconcile with upsert
        if (null == found) return;
        update.id = found.id;
        update.receivedAt = found.receivedAt;
    }
}
