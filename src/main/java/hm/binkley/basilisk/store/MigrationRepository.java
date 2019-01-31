package hm.binkley.basilisk.store;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface MigrationRepository
        extends PagingAndSortingRepository<Migration, Long> {
}
