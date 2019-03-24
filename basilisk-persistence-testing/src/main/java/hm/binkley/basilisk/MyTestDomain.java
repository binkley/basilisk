package hm.binkley.basilisk;

import hm.binkley.basilisk.store.MyTestRecord;
import hm.binkley.basilisk.store.MyTestRepository;
import hm.binkley.basilisk.store.MyTestStore;
import lombok.EqualsAndHashCode;
import lombok.Generated;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Generated // Lie to JaCoCo
@ToString(callSuper = true)
public class MyTestDomain
        extends StandardDomain<MyTestRecord, MyTestRepository, MyTestStore,
        MyTestDomain> {
    public MyTestDomain(final MyTestRecord record) {
        super(record);
    }
}
