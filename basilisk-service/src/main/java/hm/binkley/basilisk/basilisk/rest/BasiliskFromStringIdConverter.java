package hm.binkley.basilisk.basilisk.rest;

import hm.binkley.basilisk.basilisk.domain.Basilisk;
import hm.binkley.basilisk.basilisk.domain.Basilisks;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BasiliskFromStringIdConverter
        implements Converter<String, Basilisk> {
    private final Basilisks basilisks;

    @Override
    public Basilisk convert(final String id) {
        return basilisks.byId(Long.valueOf(id)).orElseThrow();
    }
}
