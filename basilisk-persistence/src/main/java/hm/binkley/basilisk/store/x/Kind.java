package hm.binkley.basilisk.store.x;

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
        implements WithNears {
    private final @NotNull KindRecord record;
    private final @NotNull Nears nears;

    public String getCode() { return record.code; }

    public BigDecimal getCoolness() { return record.coolness; }

    @Override
    public Stream<Near> getNears() {
        return record.nears.stream()
                .map(ref -> nears.byCode(ref.nearCode))
                .map(Optional::orElseThrow);
    }

    @Override
    public Stream<Near> getNetNears() {
        return getNears();
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
        near.applyInto(record::addNear);
        return this;
    }

    public Kind removeNear(final Near near) {
        near.applyInto(record::removeNear);
        return this;
    }

    public Kind applyTo(final Consumer<KindRecord> applyTo) {
        applyTo.accept(record);
        return this;
    }
}
