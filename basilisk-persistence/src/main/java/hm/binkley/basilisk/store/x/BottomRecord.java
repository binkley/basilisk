package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

/** These are <em>value objects</em>, compared by {@link #foo}. */
@EqualsAndHashCode(exclude = "middleCode")
@Table("X.BOTTOM")
@ToString
public class BottomRecord {
    public @NotNull String foo;
    public String middleCode;

    public static BottomRecord unsaved(final String foo) {
        final var unsaved = new BottomRecord();
        unsaved.foo = foo;
        return unsaved;
    }
}
