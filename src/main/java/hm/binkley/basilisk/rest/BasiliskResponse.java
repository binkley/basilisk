package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRecord;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public final class BasiliskResponse {
    private final Long id;
    private final String word;
    private final Instant at;
    private final String extra;

    public static BasiliskResponse from(
            final BasiliskService service,
            final BasiliskRecord record) {
        return builder()
                .id(record.getId())
                .word(record.getWord())
                .at(record.getAt())
                .extra(service.extra(record.getWord()))
                .build();
    }
}
