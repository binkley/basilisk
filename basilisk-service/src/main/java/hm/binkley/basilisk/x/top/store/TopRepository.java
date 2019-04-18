package hm.binkley.basilisk.x.top.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface TopRepository
        extends CrudRepository<TopRecord, String> {
    @Query("SELECT * FROM X.TOP")
    Stream<TopRecord> readAll();

    @Query("INSERT INTO X.TOP (code, name, side_code,"
            + " estimated_near_code, planned_near_code)"
            + " VALUES (:code, :name, :sideCode,"
            + " :estimatedNearCode, :plannedNearCode)"
            + " ON CONFLICT (code) DO UPDATE"
            + " SET (name, side_code, estimated_near_code, planned_near_code)"
            + " = (excluded.name, excluded.side_code,"
            + " excluded.estimated_near_code, excluded.planned_near_code)"
            + " RETURNING *")
    <S extends TopRecord> S upsert(
            @Param("code") String code, @Param("name") String name,
            @Param("sideCode") String sideCode,
            @Param("estimatedNearCode") String estimatedNearCode,
            @Param("plannedNearCode") String plannedNearCode);
}
