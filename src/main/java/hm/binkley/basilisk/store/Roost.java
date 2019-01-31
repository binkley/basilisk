package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = "id")
@Table("BASILISK.ROOST")
@ToString
public class Roost {
    @Id
    public Long id;
    public String name;
    @Column("ROOST")
    public Set<RoostMigrationRef> migrations = new HashSet<>();

    public Roost add(final Migration migration) {
        migrations.add(new RoostMigrationRef(migration.id));
        return this;
    }
}
