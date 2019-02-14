package hm.binkley.basilisk.store;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.Wither;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.lang.NonNull;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

@RepositoryRestResource
public interface BasiliskRepository
        extends CrudRepository<BasiliskRepository.BasiliskRecord, Long> {
    @Query("SELECT * FROM BASILISK.BASILISK WHERE word = :word")
    List<BasiliskRecord> findByWord(String word);

    // An example of both 1) a custom query, and 2) returning a stream
    @Query("SELECT * FROM BASILISK.BASILISK")
    Stream<BasiliskRecord> readAll();

    @Builder
    @EqualsAndHashCode(exclude = {"id", "receivedAt"})
    @Table("BASILISK.BASILISK")
    @Value
    final class BasiliskRecord {
        @Id
        @Wither
        Long id;
        @Wither
        Instant receivedAt;
        @NonNull
        String word;
        @NonNull
        Instant at;
    }
}
