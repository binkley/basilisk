package hm.binkley.basilisk.flora.service;

import hm.binkley.basilisk.flora.configuration.FloraProperties;
import hm.binkley.basilisk.flora.domain.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@EnableConfigurationProperties(FloraProperties.class)
@Service
@Validated
public class SpecialService {
    private final String dailySpecial;

    @Autowired
    public SpecialService(final FloraProperties flora) {
        dailySpecial = flora.getDailySpecial();
    }

    public boolean isDailySpecial(final @NotNull Recipe recipe) {
        return recipe.ingredients().anyMatch(ingredient ->
                dailySpecial.equals(ingredient.getName()));
    }
}
