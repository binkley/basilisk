package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Ingredient;
import hm.binkley.basilisk.flora.domain.Ingredients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IngredientFromStringIdConverter
        implements Converter<String, Ingredient> {
    private final Ingredients ingredients;

    @Override
    public Ingredient convert(final String id) {
        return ingredients.byId(Long.valueOf(id)).orElseThrow();
    }
}
