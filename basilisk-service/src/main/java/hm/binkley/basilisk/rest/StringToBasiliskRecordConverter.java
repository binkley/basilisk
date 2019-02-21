package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.domain.store.BasiliskRecord;
import hm.binkley.basilisk.domain.store.BasiliskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StringToBasiliskRecordConverter
        implements Converter<String, BasiliskRecord> {
    private final BasiliskRepository repository;

    @Override
    public BasiliskRecord convert(final String id) {
        return repository.findById(Long.valueOf(id)).orElseThrow();
    }
}
