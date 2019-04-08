package hm.binkley.basilisk.store.x;

import java.util.stream.Stream;

public interface HasNears {
    Stream<Near> getNears();

    Stream<Near> getNetNears();
}
