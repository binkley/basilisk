package hm.binkley.basilisk.flora.domain.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.stream.Stream;

@RepositoryRestResource
public interface RecipeRepository
        extends CrudRepository<RecipeRecord, Long> {
    @Query("SELECT * FROM FLORA.RECIPE WHERE name = :name")
    Optional<RecipeRecord> findByName(String name);

    @Query("SELECT * FROM FLORA.RECIPE")
    Stream<RecipeRecord> readAll();
}
