package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

@EqualsAndHashCode(exclude = {"middles", "sides", "nears"})
@RequiredArgsConstructor
@ToString(exclude = {"middles", "sides", "nears"})
public final class Top
        implements HasNears {
    private final @NotNull TopRecord record;
    private final @NotNull Middles middles;
    private final @NotNull Sides sides;
    private final @NotNull Nears nears;

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

    @Override
    public Stream<Near> getOwnNears() {
        return record.nears.stream()
                .map(ref -> nears.byCode(ref.nearCode))
                .map(Optional::orElseThrow);
    }

    @Override
    public Stream<Near> getNetNears() {
        if (record.hasNears())
            return getOwnNears();

        return getMiddles()
                .flatMap(Middle::getNetNears)
                .distinct();
    }

    public Top save() {
        record.save();
        return this;
    }

    public Top delete() {
        record.delete();
        return this;
    }

    public Top addMiddle(final Middle middle) {
        middle.applyInto(record::addMiddle);
        return this;
    }

    public Top removeMiddle(final Middle middle) {
        middle.applyInto(record::removeMiddle);
        return this;
    }

    public Top addNear(final @NotNull Near near) {
        near.applyInto(record::addNear);
        return this;
    }

    public Top removeNear(final @NotNull Near near) {
        near.applyInto(record::removeNear);
        return this;
    }
}
