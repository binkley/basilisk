package hm.binkley.basilisk.domain.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.stream.Stream;

@RepositoryRestResource
public interface BasiliskRepository
        extends CrudRepository<BasiliskRecord, Long> {
    @Query("SELECT * FROM BASILISK.BASILISK WHERE word = :word")
    List<BasiliskRecord> findByWord(String word);

    // An example of both 1) a custom query, and 2) returning a stream
    @Query("SELECT * FROM BASILISK.BASILISK")
    Stream<BasiliskRecord> readAll();
}
