package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.stream.Stream;

@RepositoryRestResource
public interface ChefRepository
        extends StandardRepository<ChefRecord, ChefRepository, ChefStore> {
    @Query("SELECT * FROM FLORA.CHEF WHERE name = :name")
    Optional<ChefRecord> findByName(String name);

    @Override
    @Query("SELECT * FROM FLORA.CHEF")
    Stream<ChefRecord> readAll();
}
