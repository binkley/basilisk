package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import static java.util.Objects.requireNonNull;

/** These are <em>value objects</em>, compared by {@link #foo}. */
@EqualsAndHashCode(exclude = "middleId")
@Table("X.BOTTOM")
@ToString
public class BottomRecord {
    public String foo;
    public Long middleId;

    public static BottomRecord unsaved(final String foo) {
        final var unsaved = new BottomRecord();
        unsaved.foo = foo;
        return unsaved;
    }

    void postParentSave(final MiddleRecord middle) {
        middleId = requireNonNull(middle.id, "Unsaved: " + middle);
    }
}
