package hm.binkley.basilisk.flora.ingredient;

import hm.binkley.basilisk.flora.ingredient.Ingredients.AsAny;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@EqualsAndHashCode
@RequiredArgsConstructor
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
@ToString
public abstract class Ingredient {
    private final IngredientRecord record;

    public final Long getId() {
        return record.getId();
    }

    public final String getCode() { return record.getCode(); }

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
