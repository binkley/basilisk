package hm.binkley.basilisk.x.middle;

import hm.binkley.basilisk.x.middle.store.BottomRecord;
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

    public Bottom insertInto(final Consumer<BottomRecord> into) {
        into.accept(record);
        return this;
    }
}
