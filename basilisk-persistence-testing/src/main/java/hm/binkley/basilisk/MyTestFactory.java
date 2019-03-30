package hm.binkley.basilisk;

import hm.binkley.basilisk.store.MyTestRecord;
import hm.binkley.basilisk.store.MyTestRepository;
import hm.binkley.basilisk.store.MyTestStore;
import lombok.Generated;

@Generated // Lie to JaCoCo
public class MyTestFactory
        extends StandardFactory<MyTestRecord, MyTestRepository, MyTestStore,
        MyTestDomain> {
    public MyTestFactory(final MyTestStore store) {
        super(store, MyTestDomain::new);
    }

    public MyTestDomain unsaved(final String code, final int number) {
        return new MyTestDomain(store.unsaved(code, number));
    }
}
