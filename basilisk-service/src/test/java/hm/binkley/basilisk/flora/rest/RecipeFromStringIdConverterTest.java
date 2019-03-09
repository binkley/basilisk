package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Recipe;
import hm.binkley.basilisk.flora.domain.Recipes;
import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeFromStringIdConverterTest {
    @Mock
    private Recipes ingredients;

    private RecipeFromStringIdConverter converter;

    @BeforeEach
    void setUp() {
        converter = new RecipeFromStringIdConverter(ingredients);
    }

    @Test
    void shouldConvert() {
        final var record = new RecipeRecord(3L, EPOCH, "SOUFFLE");
        final var domain = new Recipe(record);
        when(ingredients.byId(record.getId()))
                .thenReturn(Optional.of(domain));

        assertThat(converter.convert(String.valueOf(record.getId())))
                .isEqualTo(domain);

        verifyNoMoreInteractions(ingredients);
    }

    @Test
    void shouldNotConvert() {
        assertThatThrownBy(() -> converter.convert(String.valueOf(3L)))
                .isInstanceOf(NoSuchElementException.class);
    }
}
