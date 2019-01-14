package hm.binkley.basilisk.database;

import org.springframework.data.repository.CrudRepository;

public interface BasiliskRepository
        extends CrudRepository<BasiliskRecord, Long> {
}
