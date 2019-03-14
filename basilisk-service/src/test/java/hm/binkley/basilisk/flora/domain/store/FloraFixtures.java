package hm.binkley.basilisk.flora.domain.store;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

import static java.math.BigDecimal.ONE;
import static java.time.Instant.EPOCH;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class FloraFixtures {
    public static final Long INGREDIENT_ID = 31L;
    public static final Instant INGREDIENT_RECEIVED_AT
            = EPOCH.plusSeconds(1_000_000);
    public static final String INGREDIENT_NAME = "EGGS";
    public static final BigDecimal INGREDIENT_QUANTITY = ONE;
    public static final Long RECIPE_ID = 4L;
    public static final Long CHEF_ID = 17L;

    /** @todo Used vs unused? */
    public static IngredientRecord unsavedIngredientRecord() {
        return unsavedIngredientRecordNamed(INGREDIENT_NAME);
    }

    public static IngredientRecord unsavedIngredientRecordNamed(
            final String name) {
        return IngredientRecord.raw(name, INGREDIENT_QUANTITY, CHEF_ID);
    }

    public static IngredientRecord savedUnusedIngredientRecord() {
        return new IngredientRecord(INGREDIENT_ID,
                INGREDIENT_RECEIVED_AT, INGREDIENT_NAME,
                INGREDIENT_QUANTITY, null, CHEF_ID);
    }

    public static IngredientRecord savedUsedIngredientRecord() {
        return new IngredientRecord(INGREDIENT_ID,
                INGREDIENT_RECEIVED_AT, INGREDIENT_NAME,
                INGREDIENT_QUANTITY, RECIPE_ID, CHEF_ID);
    }
}
