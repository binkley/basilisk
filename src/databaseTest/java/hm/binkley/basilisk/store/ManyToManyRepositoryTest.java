package hm.binkley.basilisk.store;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJdbcTest
class ManyToManyRepositoryTest {
    @Autowired
    private RoostRepository roosts;
    @Autowired
    private MigrationRepository migrations;

    @SuppressFBWarnings("NP")
    @Test
    void shouldRoundtrip() {
        final var austin = new Roost();
        austin.name = "Austin";
        final var bat = new Migration();
        bat.name = "Bat";

        migrations.save(bat);

        final var batRef = new MigrationRef();
        batRef.id = bat.id;
        austin.migrations.add(batRef);

        final var found = roosts.findById(
                roosts.save(austin).id);

        assertThat(found).contains(austin);
    }
}
