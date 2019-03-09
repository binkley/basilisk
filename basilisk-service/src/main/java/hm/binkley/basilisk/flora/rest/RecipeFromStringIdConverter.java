package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Recipe;
import hm.binkley.basilisk.flora.domain.Recipes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeFromStringIdConverter
        implements Converter<String, Recipe> {
    private final Recipes ingredients;

    @Override
    public Recipe convert(final String id) {
        return ingredients.byId(Long.valueOf(id)).orElseThrow();
    }
}
