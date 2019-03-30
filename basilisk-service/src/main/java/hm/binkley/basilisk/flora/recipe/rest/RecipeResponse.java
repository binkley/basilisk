package hm.binkley.basilisk.flora.recipe.rest;

import hm.binkley.basilisk.Codeable;
import hm.binkley.basilisk.flora.ingredient.rest.UsedIngredientResponse;
import hm.binkley.basilisk.flora.recipe.Recipe;
import lombok.Builder;
import lombok.Value;

import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;

@Builder
@Value
public final class RecipeResponse
        implements Codeable<RecipeResponse> {
    Long id;
    String code;
    String name;
    boolean dailySpecial;
    Long chefId;
    @Builder.Default
    SortedSet<UsedIngredientResponse> ingredients = new TreeSet<>();

    public static RecipeResponse of(final Recipe domain,
            final boolean dailySpecial) {
        return new RecipeResponse(
                domain.getId(), domain.getCode(), domain.getName(),
                dailySpecial, domain.getChefId(),
                domain.getIngredients()
                        .map(UsedIngredientResponse::of)
                        .collect(toCollection(TreeSet::new)));
    }
}
