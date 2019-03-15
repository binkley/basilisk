package hm.binkley.basilisk.flora.domain.store;

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
    String name;
    @Getter
    BigDecimal quantity;
    @Getter
    Long recipeId;
    @Getter
    Long chefId;

    public IngredientRecord(final Long id, final Instant receivedAt,
            final String name, final BigDecimal quantity, final Long recipeId,
            final Long chefId) {
        super(() -> new IngredientRecord(id, receivedAt, name, quantity,
                        recipeId, chefId),
                id, receivedAt);
        this.name = name;
        this.quantity = quantity;
        this.recipeId = recipeId;
        this.chefId = chefId;
    }

    public static IngredientRecord raw(final String name,
            final BigDecimal quantity, final Long chefId) {
        return new IngredientRecord(null, null, name, quantity, null, chefId);
    }

    public boolean isUsed() {
        return null != recipeId;
    }
}
