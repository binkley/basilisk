package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Consumer;

@EqualsAndHashCode(exclude = "kinds")
@RequiredArgsConstructor
@ToString(exclude = "kinds")
public final class Middle {
    private final @NotNull MiddleRecord record;
    private final @NotNull Kinds kinds;

    public Long getId() { return record.id; }

    public int getMid() { return record.mid; }

    public BigDecimal getCoolness() {
        return getKind()
                .map(Kind::getCoolness)
                .orElse(null);
    }

    public Optional<Kind> getKind() {
        return Optional.ofNullable(record.kindId)
                .flatMap(kinds::byId);
    }

    public Middle save() {
        record.save();
        return this;
    }

    public Middle delete() {
        record.delete();
        return this;
    }

    public Middle define(final @NotNull Kind kind) {
        kind.defineInto(record::define);
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
