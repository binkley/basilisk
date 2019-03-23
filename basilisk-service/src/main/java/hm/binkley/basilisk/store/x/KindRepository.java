package hm.binkley.basilisk.store.x;

import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

public interface KindRepository
        extends CrudRepository<KindRecord, Long> {
    default Stream<KindRecord> readAll() {
        return stream(findAll().spliterator(), false);
    }
}
