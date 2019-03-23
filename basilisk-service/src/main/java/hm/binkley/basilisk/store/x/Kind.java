package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.function.Consumer;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Kind {
    private final @NotNull KindRecord record;

    public Long getId() { return record.id; }

    public BigDecimal getCoolness() { return record.coolness; }

    public Kind save() {
        record.save();
        return this;
    }

    public Kind delete() {
        record.delete();
        return this;
    }

    public Kind defineInto(final Consumer<KindRecord> define) {
        define.accept(record);
        return this;
    }
}
