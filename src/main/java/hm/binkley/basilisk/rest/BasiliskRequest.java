package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.store.BasiliskRecord;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Builder
@Data
@RequiredArgsConstructor
public final class BasiliskRequest {
    @Length(min = 3, max = 32)
    private final String word;
    @NotNull
    private final OffsetDateTime when;

    public BasiliskRecord toRecord() {
        return BasiliskRecord.builder()
                .word(getWord())
                .when(when.toInstant())
                .build();
    }
}
