package hm.binkley.basilisk.x.kind;

import hm.binkley.basilisk.x.kind.store.KindRecord;
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

@EqualsAndHashCode(exclude = "nears")
@RequiredArgsConstructor
@ToString(exclude = "nears")
public final class Kind
        implements HasNears {
    private final @NotNull KindRecord record;
    private final @NotNull Nears nears;

    public String getCode() { return record.code; }

    public BigDecimal getCoolness() { return record.coolness; }

    @Override
    public Stream<Near> getOwnNears() {
        return record.nears.stream()
                .map(ref -> nears.byCode(ref.nearCode))
                .map(Optional::orElseThrow);
    }

    @Override
    public Stream<Near> getNetNears() {
        return getOwnNears();
    }

    public Kind save() {
        record.save();
        return this;
    }

    public Kind delete() {
        record.delete();
        return this;
    }

    public Kind addNear(final Near near) {
        near.insertInto(record::addNear);
        return this;
    }

    public Kind removeNear(final Near near) {
        near.insertInto(record::removeNear);
        return this;
    }

    public Kind insertInto(final Consumer<KindRecord> into) {
        into.accept(record);
        return this;
    }
}
