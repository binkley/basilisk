package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

@EqualsAndHashCode(exclude = {"kinds", "sides", "nears"})
@RequiredArgsConstructor
@ToString(exclude = {"kinds", "sides", "nears"})
public final class Middle
        implements WithNears {
    private final @NotNull MiddleRecord record;
    private final @NotNull Kinds kinds;
    private final @NotNull Sides sides;
    private final @NotNull Nears nears;

    public String getCode() { return record.code; }

    public int getMid() { return record.mid; }

    public BigDecimal getCoolness() {
        return getKind()
                .map(Kind::getCoolness)
                .orElse(null);
    }

    public Instant getTime() {
        return getSide()
                .map(Side::getTime)
                .orElse(null);
    }

    public Optional<Kind> getKind() {
        return Optional.ofNullable(record.kindCode)
                .flatMap(kinds::byCode);
    }

    public Optional<Side> getSide() {
        return Optional.ofNullable(record.sideCode)
                .flatMap(sides::byCode);
    }

    public Stream<Bottom> getBottoms() {
        return record.bottoms.stream()
                .map(Bottom::new);
    }

    @Override
    public Stream<Near> getNears() {
        return record.nears.stream()
                .map(ref -> nears.byCode(ref.nearCode))
                .map(Optional::orElseThrow);
    }

    @Override
    public Stream<Near> getNetNears() {
        if (record.hasNears())
            return getNears();

        return getKind()
                .map(Kind::getNetNears)
                .orElse(Stream.empty());
    }

    public Middle save() {
        record.save();
        return this;
    }

    public Middle delete() {
        record.delete();
        return this;
    }

    public Middle attachToKind(final @NotNull Kind kind) {
        kind.applyTo(record::defineKind);
        return this;
    }

    public Middle detachFromKind() {
        record.detachFromKind();
        return this;
    }

    public Middle attachToSide(final @NotNull Side side) {
        side.applyTo(record::attachToSide);
        return this;
    }

    public Middle detachFromSide() {
        record.detachFromSide();
        return this;
    }

    public Middle addBottom(final @NotNull Bottom bottom) {
        bottom.applyTo(record::addBottom);
        return this;
    }

    public Middle addBottom(final String foo) {
        record.addBottom(BottomRecord.unsaved(foo));
        return save();
    }

    public Middle removeBottom(final @NotNull Bottom bottom) {
        bottom.applyTo(record::removeBottom);
        return this;
    }

    public Middle addNear(final @NotNull Near near) {
        near.applyInto(record::addNear);
        return this;
    }

    public Middle removeNear(final @NotNull Near near) {
        near.applyInto(record::removeNear);
        return this;
    }

    Middle applyInto(final Consumer<MiddleRecord> into) {
        into.accept(record);
        return this;
    }
}
