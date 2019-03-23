package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public final class Kind {
    private final KindRecord record;

    public BigDecimal getCoolness() { return record.coolness; }
}
