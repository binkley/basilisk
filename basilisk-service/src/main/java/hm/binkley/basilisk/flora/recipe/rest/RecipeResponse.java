package hm.binkley.basilisk.flora.recipe.rest;

import hm.binkley.basilisk.flora.ingredient.rest.UsedIngredientResponse;
import hm.binkley.basilisk.flora.recipe.Recipes.As;
import lombok.Builder;
import lombok.Value;

import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;

@Builder
@Value
public final class RecipeResponse
        implements Comparable<RecipeResponse> {
    Long id;
    String code;
    String name;
    boolean dailySpecial;
    Long chefId;
    @Builder.Default
    SortedSet<UsedIngredientResponse> ingredients = new TreeSet<>();

    public static As<RecipeResponse, UsedIngredientResponse> using(
            final boolean dailySpecial) {
        return (id, code, name, chefId, ingredients) -> new RecipeResponse(
                id, code, name, dailySpecial, chefId,
                ingredients.collect(toCollection(TreeSet::new)));
    }

    @Override
    public int compareTo(final RecipeResponse that) {
        return getCode().compareTo(that.getCode());
    }
}
