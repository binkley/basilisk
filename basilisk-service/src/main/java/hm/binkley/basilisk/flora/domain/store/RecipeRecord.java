package hm.binkley.basilisk.flora.domain.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

@EqualsAndHashCode(exclude = {"id", "receivedAt", "store"})
@Table("FLORA.RECIPE")
@ToString
public final class RecipeRecord {
    @Id
    @Getter
    Long id;
    @Getter
    Instant receivedAt;
    @Getter
    String name;
    @Getter
    String chefCode;
    @Column("recipe_id")
    @Getter
    Set<IngredientRecord> ingredients = new LinkedHashSet<>();
    @Transient
    RecipeStore store;

    public RecipeRecord(final Long id, final Instant receivedAt,
            final String name) {
        this.id = id;
        this.receivedAt = receivedAt;
        this.name = name;
    }

    public static RecipeRecord raw(final String name) {
        return new RecipeRecord(null, null, name);
    }

    public RecipeRecord add(final IngredientRecord ingredient) {
        ingredients.add(ingredient);
        return this;
    }

    public RecipeRecord addAll(final Stream<IngredientRecord> ingredients) {
        ingredients.forEach(this::add);
        return this;
    }

    public RecipeRecord save() {
        return store.save(this);
    }
}
