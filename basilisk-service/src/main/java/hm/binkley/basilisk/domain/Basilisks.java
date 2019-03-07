package hm.binkley.basilisk.domain;

import hm.binkley.basilisk.domain.store.BasiliskRecord;
import hm.binkley.basilisk.domain.store.BasiliskStore;
import hm.binkley.basilisk.domain.store.CockatriceRecord;
import hm.binkley.basilisk.rest.BasiliskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Basilisks {
    private static final BasiliskRequest.As<BasiliskRecord, CockatriceRecord>
            asBasiliskRecord = (word, at, cockatrices) ->
            BasiliskRecord.raw(word, at).addAll(cockatrices);

    private final BasiliskStore store;

    public Optional<Basilisk> byId(final Long id) {
        return store.byId(id).map(Basilisk::new);
    }

    public Stream<Basilisk> byWord(final String word) {
        return store.byWord(word).map(Basilisk::new);
    }

    public Stream<Basilisk> all() {
        return store.all().map(Basilisk::new);
    }

    public Basilisk create(final BasiliskRequest request) {
        return new Basilisk(store.save(
                request.as(asBasiliskRecord, CockatriceRecord::raw)));
    }
}
