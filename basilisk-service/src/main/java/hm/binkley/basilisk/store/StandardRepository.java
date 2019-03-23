package hm.binkley.basilisk.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public interface StandardRepository<T extends StandardRecord<T, R, S>,
        R extends StandardRepository<T, R, S>,
        S extends StandardStore<T, R, S>>
        extends CrudRepository<T, Long> {
    @Query("TODO: OVERRIDE AND SPECIFY QUERY")
    Optional<T> findByCode(String code);

    @Query("TODO: OVERRIDE AND SPECIFY QUERY")
    Stream<T> readAll();

    default T upsert(final T maybeNew,
            final BiConsumer<T, @NotNull T> prepareUpsert) {
        final var maybeFound = findByCode(maybeNew.getCode());
        if (maybeFound.isPresent()) {
            final var found = maybeFound.get();
            maybeNew.become(found);
            prepareUpsert.accept(found, maybeNew);
        } else
            prepareUpsert.accept(null, maybeNew);
        return save(maybeNew);
    }
}
