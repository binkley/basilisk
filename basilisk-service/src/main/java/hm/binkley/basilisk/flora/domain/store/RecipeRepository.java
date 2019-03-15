package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.stream.Stream;

@RepositoryRestResource
public interface RecipeRepository
        extends StandardRepository<RecipeRecord, RecipeRepository,
        RecipeStore> {
    @Query("SELECT * FROM FLORA.RECIPE WHERE name = :name")
    Optional<RecipeRecord> findByName(String name);

    @Override
    @Query("SELECT * FROM FLORA.RECIPE")
    Stream<RecipeRecord> readAll();
}
