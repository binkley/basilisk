package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public abstract class Ingredient {
    private final IngredientRecord record;

    public Long getId() {
        return record.getId();
    }

    public String getName() {
        return record.getName();
    }

    public Long getRecipeId() {
        return record.getRecipeId();
    }

    public Long getChefId() { return record.getChefId(); }

    public <I> I asAny(final Ingredient.As<I> asOther) {
        return asOther.from(getId(), getName(), getRecipeId(), getChefId());
    }

    public interface As<I> {
        I from(final Long id, final String name, final Long recipeId,
                final Long chefId);
    }
}
