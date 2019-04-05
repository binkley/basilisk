package hm.binkley.basilisk.store.x;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface NearRepository
        extends CrudRepository<NearRecord, String> {
    @Query("SELECT * FROM X.NEAR")
    Stream<NearRecord> readAll();

    @Query("INSERT INTO X.NEAR (code)"
            + " VALUES (:code)"
            + " ON CONFLICT (code) DO NOTHING"
            + " RETURNING *")
    <S extends NearRecord> S upsert(@Param("code") String code);
}
