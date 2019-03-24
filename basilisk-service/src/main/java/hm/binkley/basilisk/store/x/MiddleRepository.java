package hm.binkley.basilisk.store.x;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

public interface MiddleRepository
        extends CrudRepository<MiddleRecord, Long> {
    default Stream<MiddleRecord> readAll() {
        return stream(findAll().spliterator(), false);
    }

    @Query("SELECT * FROM X.MIDDLE m"
            + " WHERE EXISTS(SELECT 1 FROM X.TOP_MIDDLE tm"
            + " WHERE m.id = tm.middle_id)")
    Stream<MiddleRecord> findAllOwned();

    @Query("SELECT * FROM X.MIDDLE m"
            + " WHERE NOT EXISTS(SELECT 1 FROM X.TOP_MIDDLE tm"
            + " WHERE m.id = tm.middle_id)")
    Stream<MiddleRecord> findAllFree();

    /** Finds all bottoms, regardless of middle; useful only in testing. */
    @Query("SELECT * FROM X.BOTTOM")
    Stream<BottomRecord> findAllBottoms();
}
