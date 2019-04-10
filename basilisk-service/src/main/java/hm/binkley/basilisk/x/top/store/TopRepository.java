package hm.binkley.basilisk.x.top.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface TopRepository
        extends CrudRepository<TopRecord, String> {
    @Query("SELECT * FROM X.TOP")
    Stream<TopRecord> readAll();

    @Query("INSERT INTO X.TOP (code, name, side_code)"
            + " VALUES (:code, :name, :sideCode)"
            + " ON CONFLICT (code) DO UPDATE"
            + " SET (name, side_code) = (excluded.name, excluded.side_code)"
            + " RETURNING *")
    <S extends TopRecord> S upsert(
            @Param("code") String code, @Param("name") String name,
            @Param("sideCode") String sideCode);
}
