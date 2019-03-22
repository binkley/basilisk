package hm.binkley.basilisk.store;

import java.util.Optional;

interface MyTestRepository
        extends StandardRepository<MyTestRecord, MyTestRepository,
        MyTestStore> {
    Optional<MyTestRecord> findByCode(String code);
}
