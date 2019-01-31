package hm.binkley.basilisk.store;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface CityRepository
        extends PagingAndSortingRepository<City, Long> {
}
