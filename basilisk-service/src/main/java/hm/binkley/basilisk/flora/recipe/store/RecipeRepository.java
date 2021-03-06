package hm.binkley.basilisk.flora.recipe.store;

import hm.binkley.basilisk.store.StandardRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface RecipeRepository
        extends StandardRepository<RecipeRecord, RecipeRepository,
        RecipeStore> {
    @Override
    @Query("SELECT * FROM FLORA.RECIPE WHERE code = :code")
    Optional<RecipeRecord> findByCode(String code);

    @Query("SELECT * FROM FLORA.RECIPE WHERE name = :name")
    Optional<RecipeRecord> findByName(String name);
}
