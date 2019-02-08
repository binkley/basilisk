package hm.binkley.basilisk.store;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Wither;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@RequiredArgsConstructor
@Table("BASILISK.ROOST_MIGRATION")
public class RoostMigrationRef {
    @Column("MIGRATION")
    @Wither
    private final Long id;
}
