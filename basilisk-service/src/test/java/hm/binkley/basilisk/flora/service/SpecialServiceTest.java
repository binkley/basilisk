package hm.binkley.basilisk.flora.service;

import hm.binkley.basilisk.flora.domain.Recipe;
import hm.binkley.basilisk.flora.domain.UsedIngredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpecialServiceTest {
    private SpecialService service;

    @BeforeEach
    void setUp() {
        // Someday, I'll have some Spring magic injection!
        service = new SpecialService();
    }

    @Test
    void shouldNoteDailySpecial() {
        final var ingredient = mock(UsedIngredient.class);
        when(ingredient.getName())
                .thenReturn("EGGS");
        final var recipe = mock(Recipe.class);
        when(recipe.ingredients())
                .thenReturn(Stream.of(ingredient));

        assertThat(service.isDailySpecial(recipe))
                .withFailMessage("Should contain eggs")
                .isTrue();
    }

    @Test
    void shouldNoteNotDailySpecial() {
        final var ingredient = mock(UsedIngredient.class);
        when(ingredient.getName())
                .thenReturn("CAVIER");
        final var recipe = mock(Recipe.class);
        when(recipe.ingredients())
                .thenReturn(Stream.of(ingredient));

        assertThat(service.isDailySpecial(recipe))
                .withFailMessage("Should not contain eggs")
                .isFalse();
    }
}
