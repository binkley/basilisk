package hm.binkley.basilisk.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

public interface StandardRepository<T extends StandardRecord<T, R, S>,
        R extends StandardRepository<T, R, S>,
        S extends StandardStore<T, R, S>>
        extends CrudRepository<T, Long> {
    default Stream<T> readAll() {
        return stream(findAll().spliterator(), false);
    }

    @Query("TODO: OVERRIDE AND SPECIFY QUERY")
    Optional<T> findByCode(String code);

    default T upsert(final T maybeNew,
            final BiConsumer<T, @NotNull T> prepareUpsert) {
        final var maybeFound = findByCode(maybeNew.getCode())
                .orElse(null);
        if (null != maybeFound) {
            maybeNew.become(maybeFound);
        }
        prepareUpsert.accept(maybeFound, maybeNew);
        return save(maybeNew);
    }
}
