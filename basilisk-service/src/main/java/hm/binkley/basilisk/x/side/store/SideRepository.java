package hm.binkley.basilisk.x.side.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface SideRepository
        extends CrudRepository<SideRecord, String> {
    @Query("SELECT * FROM X.SIDE")
    Stream<SideRecord> readAll();

    @Query("INSERT INTO X.SIDE (code)"
            + " VALUES (:code)"
            + " ON CONFLICT (code) DO NOTHING"
            + " RETURNING *")
    <S extends SideRecord> S upsert(@Param("code") String code);
}
