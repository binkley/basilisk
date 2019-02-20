package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.domain.store.BasiliskRecord;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Builder
@Data
@RequiredArgsConstructor
public final class BasiliskRequest {
    private final @Length(min = 3, max = 32) String word;
    private final @NotNull Instant at;

    public BasiliskRecord toRecord() {
        return BasiliskRecord.builder()
                .word(word)
                .at(at)
                .build();
    }
}
