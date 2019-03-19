package hm.binkley.basilisk.store;

import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public interface StandardRepository<T extends StandardRecord<T, R, S>,
        R extends StandardRepository<T, R, S>,
        S extends StandardStore<T, R, S>>
        extends CrudRepository<T, Long> {
    Stream<T> readAll();

    default T upsert(final T maybeNew,
            final Function<T, Optional<T>> findBy,
            final BiConsumer<T, @NotNull T> prepareUpsert) {
        final var maybeFound = findBy.apply(maybeNew);
        if (maybeFound.isPresent()) {
            final var found = maybeFound.get();
            maybeNew.id = found.getId();
            maybeNew.receivedAt = found.getReceivedAt();
            prepareUpsert.accept(found, maybeNew);
        } else
            prepareUpsert.accept(null, maybeNew);
        return save(maybeNew);
    }
}
