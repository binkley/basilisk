package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(exclude = "id")
@Table("BASILISK.ANIMAL")
@ToString
public class Animal {
    @Id
    public Long id;
    public String name;
}
