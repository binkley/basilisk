package hm.binkley.basilisk.flora.domain.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@EqualsAndHashCode(exclude = {"id", "receivedAt", "recipeId", "store"})
@Table("FLORA.INGREDIENT")
@ToString
public final class IngredientRecord {
    @Id
    @Getter
    Long id;
    @Getter
    Instant receivedAt;
    @Getter
    String name;
    @Getter
    Long recipeId;
    @Transient
    IngredientStore store;

    public IngredientRecord(final Long id, final Instant receivedAt,
            final String name, final Long recipeId) {
        this.id = id;
        this.receivedAt = receivedAt;
        this.name = name;
        this.recipeId = recipeId;
    }

    public static IngredientRecord raw(final String name) {
        return new IngredientRecord(null, null, name, null);
    }

    public boolean isUsed() {
        return null != recipeId;
    }

    public IngredientRecord save() {
        return store.save(this);
    }
}
