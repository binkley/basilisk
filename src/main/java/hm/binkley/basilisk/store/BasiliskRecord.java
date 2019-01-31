package hm.binkley.basilisk.store;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.Instant;

@Builder
@Data
@EqualsAndHashCode(exclude = {"id", "receivedAt"})
@Table("BASILISK.BASILISK")
public final class BasiliskRecord {
    @Id
    @Wither
    private final Long id;
    @Wither
    private final Instant receivedAt;
    @NonNull
    private final String word;
    @NonNull
    private final Instant when;
}
