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

import static hm.binkley.basilisk.store.PersistenceTesting.simulateRepositorySave;
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
        final var saved = MyTestRecord.unsaved(CODE, NUMBER);
        saved.id = 1L;
        saved.receivedAt = EPOCH;
        when(repository.findAll()).thenReturn(
                List.of(saved));
        when(repository.readAll()).thenCallRealMethod();

        final var found = repository.readAll();

        assertThat(found).containsExactly(saved);
    }

    @Test
    void shouldInsertWhenNew() {
        final UpsertCapture prepareUpsert = new UpsertCapture();
        final var update = MyTestRecord.unsaved(CODE, NUMBER);
        when(repository.upsert(update, prepareUpsert))
                .thenCallRealMethod();
        when(repository.findByCode(update.getCode()))
                .thenReturn(Optional.empty());
        final var savedId = 1L;
        final var savedReceivedAt = EPOCH;
        when(repository.save(update)).then(simulateRepositorySave(
                savedId, savedReceivedAt));

        final var upserted = repository.upsert(update, prepareUpsert);

        assertThat(prepareUpsert.maybeFound).isNull();
        assertThat(prepareUpsert.maybeNew).isSameAs(update);
        assertThat(upserted).isEqualTo(update);
        assertThat(upserted.getId()).isEqualTo(savedId);
        assertThat(upserted.getReceivedAt()).isEqualTo(savedReceivedAt);
    }

    @Test
    void shouldUpdateWhenExisting() {
        final UpsertCapture prepareUpsert = new UpsertCapture();
        final var update = MyTestRecord.unsaved(CODE, NUMBER);
        final var found = MyTestRecord.unsaved(
                update.getCode(), update.getNumber() + 1);
        found.id = 1L;
        found.receivedAt = EPOCH;
        when(repository.upsert(update, prepareUpsert))
                .thenCallRealMethod();
        when(repository.findByCode(update.getCode()))
                .thenReturn(Optional.of(found));
        when(repository.save(update)).then(simulateRepositorySave(
                found.getId(), found.getReceivedAt()));

        final var upserted = repository.upsert(update, prepareUpsert);

        assertThat(prepareUpsert.maybeFound).isSameAs(found);
        assertThat(prepareUpsert.maybeNew).isSameAs(update);
        assertThat(upserted).isEqualTo(update);
        assertThat(upserted.getId()).isEqualTo(found.getId());
        assertThat(upserted.getReceivedAt()).isEqualTo(found.getReceivedAt());
    }

    private static final class UpsertCapture
            implements BiConsumer<MyTestRecord, @NotNull MyTestRecord> {
        private MyTestRecord maybeFound;
        private MyTestRecord maybeNew;

        @Override
        public void accept(final MyTestRecord maybeFound,
                final @NotNull MyTestRecord maybeNew) {
            this.maybeFound = maybeFound;
            this.maybeNew = maybeNew;
        }
    }
}
