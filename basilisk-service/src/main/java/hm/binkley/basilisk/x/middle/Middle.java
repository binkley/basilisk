package hm.binkley.basilisk.x.middle;

import hm.binkley.basilisk.x.kind.Kind;
import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.middle.store.BottomRecord;
import hm.binkley.basilisk.x.middle.store.MiddleRecord;
import hm.binkley.basilisk.x.middle.store.MiddleRecord.NearRef;
import hm.binkley.basilisk.x.near.HasNears;
import hm.binkley.basilisk.x.near.Near;
import hm.binkley.basilisk.x.near.Nears;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

@EqualsAndHashCode(exclude = {"kinds", "nears"})
@RequiredArgsConstructor
@ToString(exclude = {"kinds", "nears"})
public final class Middle
        implements HasNears {
    private final @NotNull MiddleRecord record;
    private final @NotNull Kinds kinds;
    private final @NotNull Nears nears;

    public String getCode() { return record.code; }

    public int getMid() { return record.mid; }

    public BigDecimal getCoolness() {
        return getKind()
                .map(Kind::getCoolness)
                .orElse(null);
    }

    public Optional<Kind> getKind() {
        return Optional.ofNullable(record.kindCode)
                .flatMap(kinds::byCode);
    }

    public Stream<Bottom> getBottoms() {
        return record.bottoms.stream()
                .map(Bottom::new);
    }

    @Override
    public Stream<Near> getOwnNears() {
        return record.nears.stream()
                .map(NearRef::getNearCode)
                .map(nears::byCode)
                .map(Optional::orElseThrow);
    }

    @Override
    public Stream<Near> getPlannedNears() {
        if (record.hasNears())
            return getOwnNears();

        return getKind()
                .map(Kind::getPlannedNears)
                .orElse(Stream.empty());
    }

    public Middle attachToKind(final @NotNull Kind kind) {
        kind.insertInto(record::defineKind);
        return this;
    }

    public Middle detachFromKind() {
        record.detachFromKind();
        return this;
    }

    public Middle addBottom(final @NotNull Bottom bottom) {
        bottom.insertInto(record::addBottom);
        return this;
    }

    public Middle addBottom(final String foo) {
        record.addBottom(BottomRecord.unsaved(foo));
        return save();
    }

    public Middle removeBottom(final @NotNull Bottom bottom) {
        bottom.insertInto(record::removeBottom);
        return this;
    }

    public Middle addNear(final @NotNull Near near) {
        near.insertInto(record::addNear);
        return this;
    }

    public Middle removeNear(final @NotNull Near near) {
        near.insertInto(record::removeNear);
        return this;
    }

    public Middle save() {
        record.save();
        return this;
    }

    public Middle delete() {
        record.delete();
        return this;
    }

    public Middle insertInto(final Consumer<MiddleRecord> into) {
        into.accept(record);
        return this;
    }
}
