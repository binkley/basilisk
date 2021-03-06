package hm.binkley.basilisk.x.kind.rest;

import hm.binkley.basilisk.x.kind.Kind;
import hm.binkley.basilisk.x.near.rest.NearResponse;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Set;
import java.util.TreeSet;

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
                        .collect(toCollection(TreeSet::new)),
                kind.getEstimatedNears()
                        .map(NearResponse::of)
                        .collect(toCollection(TreeSet::new)));
    }
}
