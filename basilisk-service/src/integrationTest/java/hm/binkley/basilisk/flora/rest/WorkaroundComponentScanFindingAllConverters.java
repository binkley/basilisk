package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.basilisk.domain.Basilisks;
import hm.binkley.basilisk.flora.domain.Ingredients;
import hm.binkley.basilisk.flora.domain.Recipes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class WorkaroundComponentScanFindingAllConverters {
    @Bean
    public Basilisks basilisks() {
        return mock(Basilisks.class);
    }

    @Bean
    public Ingredients ingredients() {
        return mock(Ingredients.class);
    }

    @Bean
    public Recipes recipes() {
        return mock(Recipes.class);
    }
}
