package hm.binkley.basilisk.store;

final class MyTestStore
        extends StandardStore<MyTestRecord, MyTestRepository,
        MyTestStore> {
    public MyTestStore(final MyTestRepository repository) {
        super(repository);
    }
}
