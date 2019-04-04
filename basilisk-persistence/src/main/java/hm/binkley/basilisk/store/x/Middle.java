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

@EqualsAndHashCode(exclude = "kinds")
@RequiredArgsConstructor
@ToString(exclude = "kinds")
public final class Middle {
    private final @NotNull MiddleRecord record;
    private final @NotNull Kinds kinds;
    private final @NotNull Sides sides;

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

    public Middle save() {
        record.save();
        return this;
    }

    public Middle delete() {
        record.delete();
        return this;
    }

    public Middle defineKind(final @NotNull Kind kind) {
        kind.defineInto(record::defineKind);
        return this;
    }

    public Middle undefineKind() {
        record.undefineKind();
        return this;
    }

    public Middle defineSide(final @NotNull Side side) {
        side.defineInto(record::defineSide);
        return this;
    }

    public Middle undefineSide() {
        record.undefineSide();
        return this;
    }

    public Middle add(final @NotNull Bottom bottom) {
        bottom.applyTo(record::add);
        return this;
    }

    public Middle remove(final @NotNull Bottom bottom) {
        bottom.applyTo(record::remove);
        return this;
    }

    public Middle addBottom(final String foo) {
        record.add(BottomRecord.unsaved(foo));
        return save();
    }

    public Middle applyInto(final Consumer<MiddleRecord> addInto) {
        addInto.accept(record);
        return this;
    }
}
