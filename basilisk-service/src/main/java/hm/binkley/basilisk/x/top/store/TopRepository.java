package hm.binkley.basilisk.x.top.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface TopRepository
        extends CrudRepository<TopRecord, String> {
    @Query("SELECT * FROM X.TOP")
    Stream<TopRecord> readAll();

    @Query("SELECT * FROM X.upsert_top(:code, :name, :plannedNearCode,"
            + " :sequenceNumber)")
    <S extends TopRecord> S upsert(
            @Param("code") String code,
            @Param("name") String name,
            @Param("plannedNearCode") String plannedNearCode,
            @Param("sequenceNumber") long sequenceNumber);
}
