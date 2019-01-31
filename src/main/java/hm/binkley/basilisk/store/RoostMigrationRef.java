package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(exclude = "id")
@Table("BASILISK.ROOST_MIGRATION")
@ToString
public class RoostMigrationRef {
    @Column("MIGRATION")
    public Long id;
}
