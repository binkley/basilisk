package hm.binkley.basilisk.flora.domain.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(exclude = {"id", "receivedAt", "store"})
@Table("FLORA.INGREDIENT")
@ToString
public final class IngredientRecord {
    @Id
    @Getter
    Long id;
    @CreatedDate
    @Getter
    Instant receivedAt;
    @Getter
    String name;
    @Getter
    BigDecimal quantity;
    @Getter
    Long recipeId;
    @Getter
    Long chefId;
    @Transient
    IngredientStore store;

    public IngredientRecord(final Long id, final Instant receivedAt,
            final String name, final BigDecimal quantity, final Long recipeId,
            final Long chefId) {
        this.id = id;
        this.receivedAt = receivedAt;
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

    public IngredientRecord save() {
        return store.save(this);
    }
}
