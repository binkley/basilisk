package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Bottom {
    private final @NotNull BottomRecord record;

    public String getFoo() { return record.foo; }

    public Bottom applyTo(final Consumer<BottomRecord> user) {
        user.accept(record);
        return this;
    }
}
