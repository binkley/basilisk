package hm.binkley.basilisk.store.x;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface MiddleRepository
        extends CrudRepository<MiddleRecord, Long> {
    @Query("SELECT * FROM X.MIDDLE m"
            + " WHERE EXISTS(SELECT 1 FROM X.TOP_MIDDLE tm"
            + " WHERE m.id = tm.middle_id)")
    Stream<MiddleRecord> findAllOwned();

    @Query("SELECT * FROM X.MIDDLE m"
            + " WHERE NOT EXISTS(SELECT 1 FROM X.TOP_MIDDLE tm"
            + " WHERE m.id = tm.middle_id)")
    Stream<MiddleRecord> findAllFree();

    @Query("SELECT * FROM X.BOTTOM")
    Stream<BottomRecord> findAllBottoms();
}
