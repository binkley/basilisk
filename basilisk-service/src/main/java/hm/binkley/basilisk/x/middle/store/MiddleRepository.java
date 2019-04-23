package hm.binkley.basilisk.x.middle.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface MiddleRepository
        extends CrudRepository<MiddleRecord, String> {
    @Query("SELECT * FROM X.MIDDLE")
    Stream<MiddleRecord> readAll();

    @Query("SELECT * FROM X.MIDDLE m"
            + " WHERE EXISTS(SELECT 1 FROM X.TOP_MIDDLE tm"
            + " WHERE m.code = tm.middle_code)")
    Stream<MiddleRecord> findAllOwned();

    @Query("SELECT * FROM X.MIDDLE m"
            + " WHERE NOT EXISTS(SELECT 1 FROM X.TOP_MIDDLE tm"
            + " WHERE m.code = tm.middle_code)")
    Stream<MiddleRecord> findAllFree();

    /** Finds all bottoms, regardless of middle; useful only in testing. */
    @Query("SELECT * FROM X.BOTTOM")
    Stream<BottomRecord> findAllBottoms();

    @Query("SELECT * FROM X.upsert_middle(:code, :kindCode, :mid,"
            + " :sequenceNumber)")
    <S extends MiddleRecord> S upsert(
            @Param("code") String code,
            @Param("kindCode") String kindCode,
            @Param("mid") Integer mid,
            @Param("sequenceNumber") long sequenceNumber);
}
