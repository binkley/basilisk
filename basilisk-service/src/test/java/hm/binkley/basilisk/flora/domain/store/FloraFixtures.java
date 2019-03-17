package hm.binkley.basilisk.flora.domain.store;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

import static java.math.BigDecimal.ONE;
import static java.time.Instant.EPOCH;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class FloraFixtures {
    public static final Long SOURCE_ID = 12L;
    public static final String SOURCE_NAME = "EGGS";
    public static final Long INGREDIENT_ID = 31L;
    public static final Instant INGREDIENT_RECEIVED_AT
            = EPOCH.plusSeconds(1_000_000);
    public static final BigDecimal INGREDIENT_QUANTITY = ONE;
    public static final Long RECIPE_ID = 4L;
    public static final Long CHEF_ID = 17L;

    public static IngredientRecord unsavedUnusedIngredientRecord() {
        return new IngredientRecord(null, null,
                SOURCE_ID, SOURCE_NAME, INGREDIENT_QUANTITY, null,
                CHEF_ID);
    }

    public static IngredientRecord unsavedUnusedIngredientRecordNamed(
            final Long sourceId, final String name) {
        return new IngredientRecord(null, null,
                sourceId, name, INGREDIENT_QUANTITY, null, CHEF_ID);
    }

    public static IngredientRecord unsavedUsedIngredientRecord() {
        return new IngredientRecord(null, null,
                SOURCE_ID, SOURCE_NAME, INGREDIENT_QUANTITY, RECIPE_ID,
                CHEF_ID);
    }

    public static IngredientRecord savedUnusedIngredientRecord() {
        final var record = unsavedUnusedIngredientRecord();
        record.id = INGREDIENT_ID;
        record.receivedAt = INGREDIENT_RECEIVED_AT;
        return record;
    }

    public static IngredientRecord savedUsedIngredientRecord() {
        final var record = unsavedUsedIngredientRecord();
        record.id = INGREDIENT_ID;
        record.receivedAt = INGREDIENT_RECEIVED_AT;
        return record;
    }
}
