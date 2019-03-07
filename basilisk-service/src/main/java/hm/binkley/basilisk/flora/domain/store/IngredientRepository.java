package hm.binkley.basilisk.flora.domain.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.stream.Stream;

@RepositoryRestResource
public interface IngredientRepository
        extends CrudRepository<IngredientRecord, Long> {
    @Query("SELECT * FROM FLORA.INGREDIENT WHERE name = :name")
    Stream<IngredientRecord> findByName(String name);

    @Query("SELECT * FROM FLORA.INGREDIENT")
    Stream<IngredientRecord> readAll();
}
