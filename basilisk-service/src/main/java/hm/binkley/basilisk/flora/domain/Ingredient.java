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

    public <I> I asAny(final Ingredient.As<I> asOther) {
        return asOther.from(getId(), getName(), getRecipeId());
    }

    public interface As<I> {
        I from(final Long id, final String name, final Long recipeId);
    }
}
