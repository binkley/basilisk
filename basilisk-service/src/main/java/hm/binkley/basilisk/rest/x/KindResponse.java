package hm.binkley.basilisk.rest.x;

import hm.binkley.basilisk.store.x.Kind;
import lombok.Value;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

@Value
public final class KindResponse {
    String code;
    BigDecimal coolness;
    Set<NearResponse> ownNears;
    Set<NearResponse> netNears;

    public static KindResponse of(final Kind kind) {
        return new KindResponse(kind.getCode(), kind.getCoolness(),
                kind.getOwnNears()
                        .map(NearResponse::of)
                        .collect(toCollection(LinkedHashSet::new)),
                kind.getNetNears()
                        .map(NearResponse::of)
                        .collect(toCollection(LinkedHashSet::new)));
    }
}
