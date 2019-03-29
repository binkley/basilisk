package hm.binkley.basilisk.flora.ingredient;

import hm.binkley.basilisk.StandardDomain;
import hm.binkley.basilisk.flora.ingredient.Ingredients.AsAny;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRepository;
import hm.binkley.basilisk.flora.ingredient.store.IngredientStore;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
@ToString(callSuper = true)
public abstract class Ingredient
        extends StandardDomain<IngredientRecord, IngredientRepository,
        IngredientStore, Ingredient> {
    public Ingredient(final IngredientRecord record) {
        super(record);
    }

    public final Long getSourceId() { return record.getSourceId(); }

    public final String getName() {
        return record.getName();
    }

    public final BigDecimal getQuantity() {
        return record.getQuantity();
    }

    public final Long getRecipeId() {
        return record.getRecipeId();
    }

    public final Long getChefId() { return record.getChefId(); }

    public final <I> I asAny(final AsAny<I> toIngredient) {
        return toIngredient.from(
                getId(), getCode(), getSourceId(), getName(), getQuantity(),
                getRecipeId(), getChefId());
    }
}

