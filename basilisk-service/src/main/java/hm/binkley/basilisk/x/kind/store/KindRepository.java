package hm.binkley.basilisk.x.kind.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.stream.Stream;

public interface KindRepository
        extends CrudRepository<KindRecord, String> {
    @Query("SELECT * FROM X.KIND")
    Stream<KindRecord> readAll();

    @Query("SELECT * FROM X.upsert_kind(:code, :coolness, :sequenceNumber)")
    <S extends KindRecord> S upsert(
            @Param("code") String code,
            @Param("coolness") BigDecimal coolness,
            @Param("sequenceNumber") long sequenceNumber);
}
