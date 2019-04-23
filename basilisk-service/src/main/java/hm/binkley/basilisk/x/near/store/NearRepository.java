package hm.binkley.basilisk.x.near.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface NearRepository
        extends CrudRepository<NearRecord, String> {
    @Query("SELECT * FROM X.NEAR")
    Stream<NearRecord> readAll();

    @Query("SELECT * FROM X.upsert_near(:code, :sequenceNumber)")
    <S extends NearRecord> S upsert(
            @Param("code") String code,
            @Param("sequenceNumber") long sequenceNumber);
}
