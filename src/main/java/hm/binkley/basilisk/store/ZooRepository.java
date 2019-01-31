package hm.binkley.basilisk.store;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ZooRepository
        extends PagingAndSortingRepository<Zoo, Long> {
}
