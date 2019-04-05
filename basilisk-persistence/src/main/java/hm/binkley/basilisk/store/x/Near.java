package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Near {
    private final @NotNull NearRecord record;

    public String getCode() { return record.code; }

    public Near save() {
        record.save();
        return this;
    }

    public Near delete() {
        record.delete();
        return this;
    }

    public Near applyInto(final Consumer<NearRecord> into) {
        into.accept(record);
        return this;
    }
}
