package hm.binkley.basilisk.flora.domain.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.stream.Stream;

@RepositoryRestResource
public interface ChefRepository
        extends CrudRepository<ChefRecord, Long> {
    @Query("SELECT * FROM FLORA.CHEF WHERE name = :name")
    Optional<ChefRecord> findByName(String name);

    @Query("SELECT * FROM FLORA.CHEF")
    Stream<ChefRecord> readAll();
}
