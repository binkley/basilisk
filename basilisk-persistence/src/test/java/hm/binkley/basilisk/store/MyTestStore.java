package hm.binkley.basilisk.store;

final class MyTestStore
        extends StandardStore<MyTestRecord, MyTestRepository,
        MyTestStore> {
    MyTestStore(final MyTestRepository repository) {
        super(repository);
    }

    MyTestRecord unsaved(final String code, final int number) {
        return bind(MyTestRecord.unsaved(code, number));
    }
}
