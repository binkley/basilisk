package hm.binkley.basilisk.domain.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.time.Instant;

@EqualsAndHashCode(exclude = {"id", "receivedAt"})
@RequiredArgsConstructor
@Table("BASILISK.BASILISK")
@ToString
public final class BasiliskRecord {
    @Id
    @Getter
    @Wither
    private final Long id;
    @Getter
    @Wither
    private final Instant receivedAt;
    @Getter
    @NonNull
    private final String word;
    @Getter
    @NonNull
    private final Instant at;

    static BasiliskRecord create(final String word, final Instant at) {
        return new BasiliskRecord(null, null, word, at);
    }
}
