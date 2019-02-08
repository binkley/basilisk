package hm.binkley.basilisk.store;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.Instant;

@Builder
@EqualsAndHashCode(exclude = {"id", "receivedAt"})
@Table("BASILISK.BASILISK")
@Value
public final class BasiliskRecord {
    @Id
    @Wither
    Long id;
    @Wither
    Instant receivedAt;
    @NonNull
    String word;
    @NonNull
    Instant at;
}
