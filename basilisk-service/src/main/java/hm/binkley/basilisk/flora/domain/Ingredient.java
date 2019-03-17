package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public abstract class Ingredient {
    private final IngredientRecord record;

    public Long getId() {
        return record.getId();
    }

    public Long getSourceId() { return record.getSourceId(); }

    public String getName() {
        return record.getName();
    }

    public BigDecimal getQuantity() {
        return record.getQuantity();
    }

    public Long getRecipeId() {
        return record.getRecipeId();
    }

    public Long getChefId() { return record.getChefId(); }

    public <I> I asAny(final Ingredient.As<I> asOther) {
        return asOther.from(getId(), getSourceId(), getName(), getQuantity(),
                getRecipeId(), getChefId());
    }

    public interface As<I> {
        I from(final Long id, final Long sourceId, final String name,
                final BigDecimal quantity, final Long recipeId,
                final Long chefId);
    }
}
