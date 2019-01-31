package hm.binkley.basilisk.store;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJdbcTest
class OneToOneRepositoryTest {
    @Autowired
    private CityRepository cities;

    @SuppressFBWarnings("NP")
    @Test
    void shouldRoundtrip() {
        final var singapore = new City();
        singapore.name = "Singapore";
        final var merlion = new Symbol();
        merlion.name = "Merlion";
        singapore.symbol = merlion;

        final var found = cities.findById(
                cities.save(singapore).id);

        assertThat(found).contains(singapore);
    }
}
