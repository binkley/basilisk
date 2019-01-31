package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(exclude = "id")
@Table("BASILISK.SEASON_MIGRATION")
@ToString
public class SeasonMigrationRef {
    @Column("MIGRATION")
    public Long id;
}
