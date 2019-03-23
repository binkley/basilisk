package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
public final class Middle {
    private final MiddleRecord record;
    private final Kinds kinds;

    public Optional<Kind> getKind() {
        return Optional.ofNullable(record.kindId)
                .flatMap(kinds::byId);
    }

    public int getMid() { return record.mid; }

    public BigDecimal getCoolness() {
        return getKind()
                .map(Kind::getCoolness)
                .orElse(null);
    }
}
