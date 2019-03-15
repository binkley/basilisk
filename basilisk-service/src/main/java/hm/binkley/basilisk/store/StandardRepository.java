package hm.binkley.basilisk.store;

import org.springframework.data.repository.CrudRepository;

import java.util.stream.Stream;

public interface StandardRepository<T extends StandardRecord<T, R, S>,
        R extends StandardRepository<T, R, S>,
        S extends StandardStore<T, R, S>>
        extends CrudRepository<T, Long> {
    Stream<T> readAll();
}
