package hm.binkley.basilisk.store;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJdbcTest
class ManyToOneRepositoryTest {
    @Autowired
    private ZooRepository zoos;

    @SuppressFBWarnings("NP")
    @Test
    void shouldRoundtrip() {
        final var houston = new Zoo();
        houston.name = "Hermann Park";
        final var lion = new Animal();
        lion.name = "Lion";
        houston.animals.add(lion);
        final var horse = new Animal();
        horse.name = "Horse";
        houston.animals.add(horse);

        final var found = zoos.findById(
                zoos.save(houston).id);

        assertThat(found).contains(houston);
    }
}
