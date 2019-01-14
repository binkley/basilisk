package hm.binkley.basilisk.database;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Builder
@Data
@EqualsAndHashCode(exclude = {"id", "receivedAt"})
@Table("BASILISK.BASILISKS")
public final class BasiliskRecord {
    @Id
    @Nullable
    @Wither
    private final Long id;
    @Nullable
    @Wither
    private final Instant receivedAt;
    @NonNull
    private final String word;
    @NonNull
    private final Instant when;
}
