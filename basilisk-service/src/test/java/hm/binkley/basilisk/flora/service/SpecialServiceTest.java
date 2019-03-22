package hm.binkley.basilisk.flora.service;

import hm.binkley.basilisk.flora.configuration.FloraProperties;
import hm.binkley.basilisk.flora.domain.Recipe;
import hm.binkley.basilisk.flora.domain.UsedIngredient;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class SpecialServiceTest {
    private static final String DAILY_SPECIAL = "MINT";

    @Mock
    private final UsedIngredient ingredient;
    @Mock
    private final Recipe recipe;

    private SpecialService service;

    @BeforeEach
    void setUp() {
        service = new SpecialService(FloraProperties.builder()
                .dailySpecial(DAILY_SPECIAL)
                .build());

        when(recipe.getIngredients())
                .thenReturn(Stream.of(ingredient));
    }

    @Test
    void shouldNoteDailySpecial() {
        when(ingredient.getName())
                .thenReturn(DAILY_SPECIAL);

        assertThat(service.isDailySpecial(recipe))
                .withFailMessage("Should contain " + DAILY_SPECIAL)
                .isTrue();
    }

    @Test
    void shouldNoteNotDailySpecial() {
        when(ingredient.getName())
                .thenReturn(DAILY_SPECIAL + "X");

        assertThat(service.isDailySpecial(recipe))
                .withFailMessage("Should not contain " + DAILY_SPECIAL)
                .isFalse();
    }
}
