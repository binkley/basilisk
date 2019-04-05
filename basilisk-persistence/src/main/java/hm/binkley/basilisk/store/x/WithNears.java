package hm.binkley.basilisk.store.x;

import java.util.stream.Stream;

public interface WithNears {
    Stream<Near> getNears();

    Stream<Near> getNetNears();
}
