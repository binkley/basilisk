package hm.binkley.basilisk.flora.source.store;

import hm.binkley.basilisk.store.StandardRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface SourceRepository
        extends StandardRepository<SourceRecord, SourceRepository,
        SourceStore> {
    @Override
    @Query("SELECT * FROM FLORA.SOURCE WHERE code = :code")
    Optional<SourceRecord> findByCode(String code);

    @Query("SELECT * FROM FLORA.SOURCE WHERE name = :name")
    Optional<SourceRecord> findByName(String name);
}
