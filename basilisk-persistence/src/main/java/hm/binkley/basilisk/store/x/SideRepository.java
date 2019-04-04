package hm.binkley.basilisk.store.x;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.stream.Stream;

public interface SideRepository
        extends CrudRepository<SideRecord, String> {
    @Query("SELECT * FROM X.SIDE")
    Stream<SideRecord> readAll();

    @Query("INSERT INTO X.SIDE (code, time)"
            + " VALUES (:code, :time)"
            + " ON CONFLICT (code) DO UPDATE"
            + " SET time = excluded.time"
            + " RETURNING *")
    <S extends SideRecord> S upsert(
            @Param("code") String code,
            @Param("time") OffsetDateTime time);
}
