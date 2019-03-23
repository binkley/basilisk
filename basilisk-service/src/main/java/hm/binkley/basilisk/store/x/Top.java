package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Stream;

@EqualsAndHashCode(exclude = "middles")
@RequiredArgsConstructor
@ToString(exclude = "middles")
public final class Top {
    private final @NotNull TopRecord record;
    private final @NotNull Middles middles;

    /** @todo More elegant way than exposing this details? */
    public Long getId() { return record.id; }

    public String getName() { return record.name; }

    public Stream<Middle> getMiddles() {
        return record.middles.stream()
                .map(ref -> middles.byId(ref.middleId))
                .map(Optional::orElseThrow);
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
