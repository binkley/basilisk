package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.domain.store.BasiliskRepository.BasiliskRecord;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public final class BasiliskResponse {
    Long id;
    String word;
    Instant at;
    String extra;

    static BasiliskResponse from(
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
