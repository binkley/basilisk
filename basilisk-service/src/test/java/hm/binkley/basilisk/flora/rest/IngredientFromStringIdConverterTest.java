package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Ingredients;
import hm.binkley.basilisk.flora.domain.UsedIngredient;
import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
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
class IngredientFromStringIdConverterTest {
    @Mock
    private Ingredients ingredients;

    private IngredientFromStringIdConverter converter;

    @BeforeEach
    void setUp() {
        converter = new IngredientFromStringIdConverter(ingredients);
    }

    @Test
    void shouldConvert() {
        final var record = new IngredientRecord(3L, EPOCH, "EGGS", 2L);
        final var domain = new UsedIngredient(record);
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
