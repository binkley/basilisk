package hm.binkley.basilisk.basilisk.domain.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.stream.Stream;

@RepositoryRestResource
public interface BasiliskRepository
        extends CrudRepository<BasiliskRecord, Long> {
    @Query("SELECT * FROM BASILISK.BASILISK WHERE word = :word")
    Stream<BasiliskRecord> findByWord(String word);

    @Query("SELECT * FROM BASILISK.BASILISK")
    Stream<BasiliskRecord> readAll();
}
