package hm.binkley.basilisk.flora.chef;

import hm.binkley.basilisk.StandardDomain;
import hm.binkley.basilisk.flora.chef.store.ChefRecord;
import hm.binkley.basilisk.flora.chef.store.ChefRepository;
import hm.binkley.basilisk.flora.chef.store.ChefStore;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class Chef
        extends StandardDomain<
        ChefRecord, ChefRepository, ChefStore, Chef> {
    public Chef(final ChefRecord record) {
        super(record);
    }

    public String getName() { return record.getName(); }
}
