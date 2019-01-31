package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(exclude = "id")
@Table("BASILISK.SYMBOL")
@ToString
public class Symbol {
    @Id
    public long id;
    public String name;
}
