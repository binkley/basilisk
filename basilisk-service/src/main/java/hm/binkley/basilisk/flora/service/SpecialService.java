package hm.binkley.basilisk.flora.service;

import hm.binkley.basilisk.flora.domain.Recipe;
import org.springframework.stereotype.Service;

@Service
public class SpecialService {
    public boolean isDailySpecial(final Recipe recipe) {
        return recipe.ingredients()
                .anyMatch(ingredient -> "EGGS".equals(ingredient.getName()));
    }
}
