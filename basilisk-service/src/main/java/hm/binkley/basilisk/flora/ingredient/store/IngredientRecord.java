package hm.binkley.basilisk.flora.ingredient.store;

import hm.binkley.basilisk.store.StandardRecord;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Table("FLORA.INGREDIENT")
@ToString
public final class IngredientRecord
        extends StandardRecord<IngredientRecord, IngredientRepository,
        IngredientStore> {
    @Getter
    public Long recipeId; // TODO: Resolve automating setting this
    @Getter
    Long sourceId;
    @Getter
    String name;
    @Getter
    BigDecimal quantity;
    @Getter
    Long chefId;

    public IngredientRecord(final Long id, final Instant receivedAt,
            final String code, final Long sourceId, final String name,
            final BigDecimal quantity, final Long recipeId,
            final Long chefId) {
        super(id, receivedAt, code);
        this.code = code;
        this.sourceId = sourceId;
        this.name = name;
        this.quantity = quantity;
        this.recipeId = recipeId;
        this.chefId = chefId;
    }

    public static IngredientRecord unsaved(final String code,
            final Long sourceId, final String name, final BigDecimal quantity,
            final Long chefId) {
        return new IngredientRecord(null, null, code, sourceId, name,
                quantity, null, chefId);
    }

    public boolean isUsed() {
        return null != recipeId;
    }
}
