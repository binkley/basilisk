package hm.binkley.basilisk.x.side.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface SideRepository
        extends CrudRepository<SideRecord, String> {
    @Query("SELECT * FROM X.SIDE")
    Stream<SideRecord> readAll();

    @Query("SELECT * FROM X.upsert_side(:code, :sequenceNumber)")
    <S extends SideRecord> S upsert(
            @Param("code") String code,
            @Param("sequenceNumber") long sequenceNumber);
}
