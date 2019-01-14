package hm.binkley.basilisk.store;

import org.springframework.data.repository.CrudRepository;

public interface BasiliskRepository
        extends CrudRepository<BasiliskRecord, Long> {
}
