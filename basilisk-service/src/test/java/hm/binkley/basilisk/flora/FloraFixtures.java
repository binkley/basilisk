package hm.binkley.basilisk.flora;

import hm.binkley.basilisk.flora.chef.store.ChefRecord;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.location.store.LocationRecord;
import hm.binkley.basilisk.flora.recipe.store.RecipeRecord;
import hm.binkley.basilisk.flora.source.store.SourceRecord;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

import static java.math.BigDecimal.ONE;
import static java.time.Instant.EPOCH;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class FloraFixtures {
    public static final Long CHEF_ID = 17L;
    public static final Instant CHEF_RECIEVED_AT
            = EPOCH.plusSeconds(800_000L);
    public static final String CHEF_CODE = "CAN";
    public static final String CHEF_NAME = "Chef Boyardee";
    public static final Long SOURCE_ID = 12L;
    public static final Instant SOURCE_RECEIVED_AT
            = EPOCH.plusSeconds(900_000L);
    public static final String SOURCE_CODE = "FARM#123";
    public static final String SOURCE_NAME = "MEDIUM EGGS";
    public static final Long INGREDIENT_ID = 31L;
    public static final Instant INGREDIENT_RECEIVED_AT
            = EPOCH.plusSeconds(1_000_000L);
    public static final String INGREDIENT_CODE = "EGG#123";
    public static final BigDecimal INGREDIENT_QUANTITY = ONE;
    public static final Long RECIPE_ID = 4L;
    public static final Instant RECIPE_RECEIVED_AT
            = EPOCH.plusSeconds(1_100_000L);
    public static final String RECIPE_CODE = "SFL#123";
    public static final String RECIPE_NAME = "SOUFFLE";
    public static final Long LOCATION_ID = 42L;
    public static final Instant LOCATION_RECEIVED_AT
            = EPOCH.plusSeconds(1_200_000L);
    public static final String LOCATION_CODE = "DAL";
    public static final String LOCATION_NAME = "The Dallas Yellow Rose";

    public static ChefRecord unsavedChefRecord() {
        return new ChefRecord(null, null, CHEF_CODE, CHEF_NAME);
    }

    public static ChefRecord savedChefRecord() {
        final var saved = unsavedChefRecord();
        saved.id = CHEF_ID;
        saved.receivedAt = CHEF_RECIEVED_AT;
        return saved;
    }

    public static IngredientRecord unsavedUnusedIngredientRecord() {
        return new IngredientRecord(null, null,
                INGREDIENT_CODE, SOURCE_ID, SOURCE_NAME, INGREDIENT_QUANTITY,
                null, CHEF_ID);
    }

    public static IngredientRecord unsavedUnusedIngredientRecordNamed(
            final Long sourceId, final String name) {
        return new IngredientRecord(null, null,
                INGREDIENT_CODE, sourceId, name, INGREDIENT_QUANTITY, null,
                CHEF_ID);
    }

    public static IngredientRecord unsavedUsedIngredientRecord() {
        return new IngredientRecord(null, null,
                INGREDIENT_CODE, SOURCE_ID, SOURCE_NAME, INGREDIENT_QUANTITY,
                RECIPE_ID, CHEF_ID);
    }

    public static IngredientRecord savedUnusedIngredientRecord() {
        final var saved = unsavedUnusedIngredientRecord();
        saved.id = INGREDIENT_ID;
        saved.receivedAt = INGREDIENT_RECEIVED_AT;
        return saved;
    }

    public static IngredientRecord savedUsedIngredientRecord() {
        final var saved = unsavedUsedIngredientRecord();
        saved.id = INGREDIENT_ID;
        saved.receivedAt = INGREDIENT_RECEIVED_AT;
        return saved;
    }

    public static SourceRecord unsavedSourceRecord() {
        return new SourceRecord(null, null, SOURCE_CODE, SOURCE_NAME);
    }

    public static SourceRecord savedSourceRecord() {
        final var saved = unsavedSourceRecord();
        saved.id = SOURCE_ID;
        saved.receivedAt = SOURCE_RECEIVED_AT;
        return saved;
    }

    public static LocationRecord unsavedLocationRecord() {
        return new LocationRecord(null, null, LOCATION_CODE, LOCATION_NAME);
    }

    public static LocationRecord savedLocationRecord() {
        final var saved = unsavedLocationRecord();
        saved.id = LOCATION_ID;
        saved.receivedAt = LOCATION_RECEIVED_AT;
        return saved;
    }

    public static RecipeRecord unsavedRecipeRecord() {
        return new RecipeRecord(null, null,
                RECIPE_CODE, RECIPE_NAME, CHEF_ID);
    }

    public static RecipeRecord savedRecipeRecord() {
        final var saved = unsavedRecipeRecord();
        saved.id = RECIPE_ID;
        saved.receivedAt = RECIPE_RECEIVED_AT;
        return saved;
    }
}
