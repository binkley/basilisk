package hm.binkley.basilisk.x.near;

import java.util.stream.Stream;

public interface HasNears {
    boolean hasOwnNears();

    default Stream<Near> getEstimatedNears() {
        return hasOwnNears() ? getOwnNears() : getOthersNears();
    }

    Stream<Near> getOwnNears();

    Stream<Near> getOthersNears();
}
