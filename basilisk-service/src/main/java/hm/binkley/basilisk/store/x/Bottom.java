package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Bottom {
    private final BottomRecord record;

    public String getFoo() { return record.foo; }
}
