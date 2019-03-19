package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StandardRepositoryTest {
    @Mock
    private MyTestRepository repository;

    @Test
    void shouldInsertWhenNew() {
        final var update = new MyTestRecord(null, null, "ABC");
        final Function<MyTestRecord, Optional<MyTestRecord>> findBy
                = this::matching;
        final BiConsumer<MyTestRecord, @NotNull MyTestRecord> prepareUpsert
                = this::replaceWith;
        when(repository.upsert(update, findBy, prepareUpsert))
                .thenCallRealMethod();
        when(repository.findByCode(update.code))
                .thenReturn(Optional.empty());
        final var saved = new MyTestRecord(1L, EPOCH, update.code);
        when(repository.save(update))
                .thenReturn(saved);

        final var upserted = repository.upsert(update, findBy, prepareUpsert);

        assertThat(upserted).isEqualTo(saved);
    }

    @Test
    void shouldUpdateWhenExisting() {
        final var update = new MyTestRecord(null, null, "ABC");
        final Function<MyTestRecord, Optional<MyTestRecord>> findBy
                = this::matching;
        final BiConsumer<MyTestRecord, @NotNull MyTestRecord> prepareUpsert
                = this::replaceWith;
        final var found = new MyTestRecord(1L, EPOCH, update.code);
        when(repository.upsert(update, findBy, prepareUpsert))
                .thenCallRealMethod();
        when(repository.findByCode(update.code))
                .thenReturn(Optional.of(found));
        when(repository.save(update))
                .thenReturn(update);

        final var upserted = repository.upsert(update, findBy, prepareUpsert);

        assertThat(upserted).isEqualTo(found);
    }

    private Optional<MyTestRecord> matching(final MyTestRecord maybeNew) {
        return repository.findByCode(maybeNew.code);
    }

    private void replaceWith(final MyTestRecord found,
            final @NotNull MyTestRecord update) {
        if (null == found) return;
        update.id = found.id;
        update.receivedAt = found.receivedAt;
    }

    interface MyTestRepository
            extends StandardRepository<MyTestRecord, MyTestRepository,
            MyTestStore> {
        Optional<MyTestRecord> findByCode(String code);
    }

    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    static final class MyTestRecord
            extends StandardRecord<MyTestRecord, MyTestRepository,
            MyTestStore> {
        String code;

        MyTestRecord(final Long id, final Instant receivedAt,
                final String code) {
            super(() -> new MyTestRecord(id, receivedAt, code),
                    id, receivedAt);
        }
    }

    class MyTestStore
            extends StandardStore<MyTestRecord, MyTestRepository,
            MyTestStore> {
        public MyTestStore() {
            super(repository);
        }
    }
}
