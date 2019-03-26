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
    @Query("TODO: OVERRIDE AND SPECIFY QUERY")
    Optional<T> findByCode(String code);

    /**
     * <ol>
     * <li>If, under the hood, Data JDBC does pull lazily from DB when the
     * return is a Stream, consider having subclasses implement</li>
     * <li>If Data JDBC just takes a collection to make a stream, keep this
     * default method</li>
     * <li>Consider cases where keeping the DB read lock open causes
     * issues</li>
     * </ol>
     *
     * @todo Todo -- require implementers to provide this; Spring directly
     * handles returning Stream, and will lazy-load
     * @see <a href="https://www.baeldung.com/spring-data-java-8"><cite>Spring
     * Data Java 8 Support</cite></a>
     */
    default Stream<T> readAll() {
        return stream(findAll().spliterator(), false);
    }

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
