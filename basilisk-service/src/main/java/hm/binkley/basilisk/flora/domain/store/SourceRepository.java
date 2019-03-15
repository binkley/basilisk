package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.stream.Stream;

@RepositoryRestResource
public interface SourceRepository
        extends StandardRepository<SourceRecord, SourceRepository,
        SourceStore> {
    @Query("SELECT * FROM FLORA.SOURCE WHERE name = :name")
    Optional<SourceRecord> findByName(String name);

    @Override
    @Query("SELECT * FROM FLORA.SOURCE")
    Stream<SourceRecord> readAll();
}
