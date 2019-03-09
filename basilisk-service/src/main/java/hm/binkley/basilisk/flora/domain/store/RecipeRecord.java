package hm.binkley.basilisk.flora.domain.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

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

    public RecipeRecord save() {
        return store.save(this);
    }
}
