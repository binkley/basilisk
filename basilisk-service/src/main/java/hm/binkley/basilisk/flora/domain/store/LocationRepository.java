package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.stream.Stream;

@RepositoryRestResource
public interface LocationRepository
        extends StandardRepository<LocationRecord, LocationRepository,
        LocationStore> {
    @Query("SELECT * FROM FLORA.LOCATION WHERE name = :name")
    Optional<LocationRecord> findByName(String name);

    @Override
    @Query("SELECT * FROM FLORA.LOCATION")
    Stream<LocationRecord> readAll();
}
