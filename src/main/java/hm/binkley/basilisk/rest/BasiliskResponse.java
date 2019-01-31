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
    private final Instant when;
    private final String extra;

    public static BasiliskResponse from(
            final BasiliskService service,
            final BasiliskRecord record) {
        return BasiliskResponse.builder()
                .id(record.getId())
                .word(record.getWord())
                .when(record.getWhen())
                .extra(service.extra(record.getWord()))
                .build();
    }
}
