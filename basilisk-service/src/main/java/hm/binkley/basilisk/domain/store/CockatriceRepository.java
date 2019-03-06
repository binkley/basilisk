package hm.binkley.basilisk.domain.store;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.stream.Stream;

@RepositoryRestResource
public interface CockatriceRepository
        extends CrudRepository<CockatriceRecord, Long> {
    @Query("SELECT * FROM BASILISK.COCKATRICE")
    Stream<CockatriceRecord> readAll();
}
