package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.store.BasiliskRecord;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Builder
@RequiredArgsConstructor
@Value
public final class BasiliskRequest {
    @Length(min = 3, max = 32) String word;
    @NotNull Instant at;

    public BasiliskRecord toRecord() {
        return BasiliskRecord.builder()
                .word(word)
                .at(at)
                .build();
    }
}
