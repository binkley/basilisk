package hm.binkley.basilisk.x.near;

import java.util.stream.Stream;

public interface HasNears {
    Stream<Near> getOwnNears();

    Stream<Near> getPlannedNears();
}
