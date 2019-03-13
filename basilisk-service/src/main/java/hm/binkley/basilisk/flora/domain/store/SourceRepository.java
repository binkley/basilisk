package hm.binkley.basilisk.flora.domain.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.stream.Stream;

@RepositoryRestResource
public interface SourceRepository
        extends CrudRepository<SourceRecord, Long> {
    @Query("SELECT * FROM FLORA.SOURCE WHERE name = :name")
    Optional<SourceRecord> findByName(String name);

    @Query("SELECT * FROM FLORA.SOURCE")
    Stream<SourceRecord> readAll();
}
