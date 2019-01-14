package hm.binkley.basilisk.store;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BasiliskRepository
        extends CrudRepository<BasiliskRecord, Long> {
    List<BasiliskRecord> findByWord(String word);
}
