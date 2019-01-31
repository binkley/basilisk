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
    public Set<MigrationRef> migrations = new HashSet<>();
}
