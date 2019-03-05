package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = "id")
@Table("BASILISK.ROOST")
@ToString
public class Season {
    @Id
    public Long id;
    public String name;
    @Column("SEASON")
    public Set<SeasonMigrationRef> migrations = new LinkedHashSet<>();
}
