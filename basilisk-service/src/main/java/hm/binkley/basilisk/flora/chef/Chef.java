package hm.binkley.basilisk.flora.chef;

import hm.binkley.basilisk.flora.chef.store.ChefRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Chef {
    private final ChefRecord record;

    public Long getId() { return record.getId(); }

    public String getCode() { return record.getCode(); }

    public String getName() { return record.getName(); }

    public Chef save() {
        record.save();
        return this;
    }

    public Chef delete() {
        record.delete();
        return this;
    }
}
