package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

@EqualsAndHashCode(exclude = {"middles", "sides", "nears"})
@RequiredArgsConstructor
@ToString(exclude = {"middles", "sides", "nears"})
public final class Top
        implements HasNears {
    private final @NotNull TopRecord record;
    private final @NotNull Middles middles;
    private final @NotNull Sides sides;
    private final @NotNull Nears nears;

    private static Collector<Near, ?, Set<Near>> toSortedSet() {
        return toCollection(TreeSet::new);
    }

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
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    public Stream<Near> getNetNears() {
        if (record.hasNears())
            return getOwnNears();

        // TODO: Better solution for intersection of streams
        final var remaining = getMiddles()
                .map(Middle::getNetNears)
                .collect(toList());
        if (remaining.isEmpty()) return Stream.empty();
        final var first = remaining.remove(0);
        if (remaining.isEmpty()) return first;

        return remaining.stream()
                .map(s -> s.collect(toSortedSet()))
                .filter(not(Collection::isEmpty))
                .collect(() -> first.collect(toSortedSet()),
                        Set::retainAll, Set::retainAll)
                .stream();
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
