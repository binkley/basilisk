package hm.binkley.basilisk.x.top;

import hm.binkley.basilisk.x.middle.Middle;
import hm.binkley.basilisk.x.middle.Middles;
import hm.binkley.basilisk.x.near.HasNears;
import hm.binkley.basilisk.x.near.Near;
import hm.binkley.basilisk.x.near.Nears;
import hm.binkley.basilisk.x.top.store.TopRecord;
import hm.binkley.basilisk.x.top.store.TopRecord.NearRef;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.util.streamex.StreamEx;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toCollection;
import static one.util.streamex.MoreCollectors.intersecting;

@EqualsAndHashCode(exclude = {"middles", "nears"})
@RequiredArgsConstructor
@ToString(exclude = {"middles", "nears"})
public final class Top
        implements HasNears {
    private final @NotNull TopRecord record;
    private final @NotNull Middles middles;
    private final @NotNull Nears nears;

    private static <T extends Comparable<T>> Collector<T, ?, Set<T>> toSortedSet() {
        return toCollection(TreeSet::new);
    }

    private static <T extends Comparable<T>>
    Stream<T> sortedIntersectionOfNonEmpty(final Stream<Stream<T>> streams) {
        return StreamEx.of(streams)
                .map(stream -> stream.collect(toSortedSet()))
                .filter(not(Collection::isEmpty))
                .collect(intersecting())
                .stream();
    }

    /** @todo More elegant way than exposing this details? */
    public String getCode() { return record.code; }

    public String getName() { return record.name; }

    public Stream<Middle> getMiddles() {
        return record.middles.stream()
                .map(ref -> middles.byCode(ref.middleCode))
                .map(Optional::orElseThrow);
    }

    public boolean isPlanned() { return null != record.plannedNearCode; }

    public Optional<Near> getPlannedNear() {
        // It is optional to have a planned near, but it if present, the
        // planned near must exist
        return Optional.ofNullable(record.plannedNearCode)
                .map(nears::byCode)
                .map(Optional::orElseThrow);
    }

    @Override
    public boolean hasOwnNears() {
        return !record.nears.isEmpty();
    }

    @Override
    public Stream<Near> getOwnNears() {
        return record.nears.stream()
                .map(NearRef::getNearCode)
                .map(nears::byCode)
                .map(Optional::orElseThrow);
    }

    @Override
    public Stream<Near> getOthersNears() {
        return sortedIntersectionOfNonEmpty(getMiddles()
                .map(Middle::getEstimatedNears));
    }

    public Top planWith(final Near near) {
        near.insertInto(record::planNear);
        return this;
    }

    public Top addMiddle(final Middle middle) {
        middle.insertInto(record::addMiddle);
        return this;
    }

    public Top removeMiddle(final Middle middle) {
        middle.insertInto(record::removeMiddle);
        return this;
    }

    public Top addNear(final @NotNull Near near) {
        near.insertInto(record::addNear);
        return this;
    }

    public Top removeNear(final @NotNull Near near) {
        near.insertInto(record::removeNear);
        return this;
    }

    public Top save() {
        record.save();
        return this;
    }

    public Top delete() {
        record.delete();
        return this;
    }
}
