package hm.binkley.basilisk.store;

import org.springframework.data.repository.CrudRepository;

public interface MigrationRepository
        extends CrudRepository<Migration, Long> {
}
