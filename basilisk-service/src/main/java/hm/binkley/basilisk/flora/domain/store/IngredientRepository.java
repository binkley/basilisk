package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardRepository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;
import java.util.stream.Stream;

@RepositoryRestResource
public interface IngredientRepository
        extends StandardRepository<IngredientRecord, IngredientRepository,
        IngredientStore> {
    @Override
    @Query("SELECT * FROM FLORA.INGREDIENT WHERE code = :code")
    Optional<IngredientRecord> findByCode(String code);

    @Query("SELECT * FROM FLORA.INGREDIENT WHERE name = :name")
    Stream<IngredientRecord> findAllByName(String name);

    @Override
    @Query("SELECT * FROM FLORA.INGREDIENT")
    Stream<IngredientRecord> readAll();

    @Query("SELECT * FROM FLORA.INGREDIENT WHERE recipe_id IS NULL")
    Stream<IngredientRecord> findAllByRecipeIdIsNull();
}
