package hm.binkley.basilisk.store.x;

import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

public interface TopRepository
        extends CrudRepository<TopRecord, Long> {
    default Stream<TopRecord> readAll() {
        return stream(findAll().spliterator(), false);
    }
}
