package hm.binkley.basilisk.flora.chef;

import org.junit.jupiter.api.Test;

import static hm.binkley.basilisk.flora.FloraFixtures.savedChefRecord;
import static org.assertj.core.api.Assertions.assertThat;

class ChefTest {
    @Test
    void shouldAs() {
        final var record = savedChefRecord();
        // The types are immaterial, just that the transformation worked
        final var targetChef = 1;

        @SuppressWarnings("PMD") final var that = new Chef(record).as(
                (id, code, name) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(code).isEqualTo(record.getCode());
                    assertThat(name).isEqualTo(record.getName());
                    return targetChef;
                });

        assertThat(that).isSameAs(targetChef);
    }
}
