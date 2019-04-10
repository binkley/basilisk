package hm.binkley.basilisk.x.side;

import hm.binkley.basilisk.x.side.store.SideRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.function.Function;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Side {
    private final @NotNull SideRecord record;

    public String getCode() { return record.code; }

    public Instant getTime() { return record.time; }

    public Side save() {
        record.save();
        return this;
    }

    public Side delete() {
        record.delete();
        return this;
    }

    public <T> T applyTo(final Function<SideRecord, T> to) {
        return to.apply(record);
    }
}
