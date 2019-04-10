package hm.binkley.basilisk.x.near;

import hm.binkley.basilisk.x.near.store.NearRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Near
        implements Comparable<Near> {
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

    @Override
    public int compareTo(final Near that) {
        return getCode().compareTo(that.getCode());
    }
}
