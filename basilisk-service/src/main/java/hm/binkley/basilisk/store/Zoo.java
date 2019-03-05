package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = "id")
@Table("BASILISK.CITY")
@ToString
public class Zoo {
    @Id
    public Long id;
    public String name;
    @Column("ZOO")
    public Set<Animal> animals = new LinkedHashSet<>();
}
