package hm.binkley.basilisk.store.x;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface TopRepository
        extends CrudRepository<TopRecord, String> {
    @Query("SELECT * FROM X.TOP")
    Stream<TopRecord> readAll();

    @Query("INSERT INTO X.TOP (code, name)"
            + " VALUES (:code, :name)"
            + " ON CONFLICT (code) DO UPDATE"
            + " SET name = excluded.name"
            + " RETURNING *")
    <S extends TopRecord> S upsert(
            @Param("code") String code, @Param("name") String name);
}
