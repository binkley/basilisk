package hm.binkley.basilisk.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.stream.Stream;

@RepositoryRestResource
public interface BasiliskRepository
        extends PagingAndSortingRepository<BasiliskRecord, Long> {
    List<BasiliskRecord> findByWord(String word);

    // An example of both 1) a custom query, and 2) returning a stream
    @Query("SELECT * FROM BASILISK.BASILISK")
    Stream<BasiliskRecord> readAll();
}
