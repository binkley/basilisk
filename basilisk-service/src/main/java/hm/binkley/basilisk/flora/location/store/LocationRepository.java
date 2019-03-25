package hm.binkley.basilisk.flora.location.store;

import hm.binkley.basilisk.store.StandardRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface LocationRepository
        extends StandardRepository<LocationRecord, LocationRepository,
        LocationStore> {
    @Override
    @Query("SELECT * FROM FLORA.LOCATION WHERE code = :code")
    Optional<LocationRecord> findByCode(String code);

    @Query("SELECT * FROM FLORA.LOCATION WHERE name = :name")
    Optional<LocationRecord> findByName(String name);
}
