package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

@EqualsAndHashCode(exclude = "middles")
@RequiredArgsConstructor
@ToString(exclude = "middles")
public final class Top {
    private final @NotNull TopRecord record;
    private final @NotNull Middles middles;
    private final @NotNull Sides sides;

    /** @todo More elegant way than exposing this details? */
    public String getCode() { return record.code; }

    public String getName() { return record.name; }

    public Instant getTime() {
        return getSide().getTime();
    }

    public Stream<Middle> getMiddles() {
        return record.middles.stream()
                .map(ref -> middles.byCode(ref.middleCode))
                .map(Optional::orElseThrow);
    }

    public Side getSide() {
        return sides.byCode(record.sideCode).orElseThrow();
    }

    public Top save() {
        record.save();
        return this;
    }

    public Top delete() {
        record.delete();
        return this;
    }

    public Top add(final Middle middle) {
        middle.applyInto(record::add);
        return this;
    }

    public Top remove(final Middle middle) {
        middle.applyInto(record::remove);
        return this;
    }
}
