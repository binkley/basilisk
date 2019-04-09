package hm.binkley.basilisk.rest.x;

import hm.binkley.basilisk.store.x.Near;
import lombok.Value;

@Value
public final class NearResponse
        implements Comparable<NearResponse> {
    String code;

    public static NearResponse of(final Near near) {
        return new NearResponse(near.getCode());
    }

    @Override
    public int compareTo(final NearResponse that) {
        return code.compareTo(that.code);
    }
}
