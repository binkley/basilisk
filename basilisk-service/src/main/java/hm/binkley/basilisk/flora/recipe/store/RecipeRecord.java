package hm.binkley.basilisk.flora.recipe.store;

import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.store.StandardRecord;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = false)
@Table("FLORA.RECIPE")
@ToString(callSuper = true)
public final class RecipeRecord
        extends StandardRecord<RecipeRecord, RecipeRepository, RecipeStore> {
    @Getter
    String name;
    @Getter
    Long chefId;
    @Column("recipe_id")
    @Getter
    SortedSet<IngredientRecord> ingredients = new TreeSet<>();

    public RecipeRecord(final Long id, final Instant receivedAt,
            final String code, final String name, final Long chefId) {
        super(id, receivedAt, code);
        this.name = name;
        this.chefId = chefId;
    }

    public static RecipeRecord unsaved(
            final String code, final String name, final Long chefId) {
        return new RecipeRecord(null, null, code, name, chefId);
    }

    public RecipeRecord addUnusedIngredient(
            final IngredientRecord ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    public RecipeRecord addAllUnusedIngredients(
            final Stream<IngredientRecord> ingredients) {
        ingredients.forEach(this::addUnusedIngredient);
        return this;
    }
}
