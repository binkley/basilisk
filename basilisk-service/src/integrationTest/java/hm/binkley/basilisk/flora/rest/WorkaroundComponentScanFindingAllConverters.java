package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.basilisk.domain.Basilisks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class WorkaroundComponentScanFindingAllConverters {
    @Bean
    public Basilisks basilisks() {
        return mock(Basilisks.class);
    }
}
