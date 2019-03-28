package hm.binkley.basilisk.store;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@EqualsAndHashCode
@Table("BASILISK.ROOST_MIGRATION")
@ToString
public class RoostMigrationRef {
    @Column("MIGRATION")
    public Long id;
}
