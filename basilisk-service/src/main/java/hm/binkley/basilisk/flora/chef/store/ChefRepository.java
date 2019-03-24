package hm.binkley.basilisk.flora.chef.store;

import hm.binkley.basilisk.store.StandardRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface ChefRepository
        extends StandardRepository<ChefRecord, ChefRepository, ChefStore> {
    @Override
    @Query("SELECT * FROM FLORA.CHEF WHERE code = :code")
    Optional<ChefRecord> findByCode(String code);

    @Query("SELECT * FROM FLORA.CHEF WHERE name = :name")
    Optional<ChefRecord> findByName(String name);
}
