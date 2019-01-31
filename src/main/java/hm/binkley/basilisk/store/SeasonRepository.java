package hm.binkley.basilisk.store;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface SeasonRepository
        extends PagingAndSortingRepository<Season, Long> {
}
