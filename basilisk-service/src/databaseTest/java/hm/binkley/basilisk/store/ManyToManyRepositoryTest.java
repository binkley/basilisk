package hm.binkley.basilisk.store;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ManyToManyRepositoryTest {
    private final RoostRepository roosts;
    private final SeasonRepository seasons;
    private final MigrationRepository migrations;

    @SuppressFBWarnings("NP")
    @Test
    void shouldBelongToMultipleAggregateRootTypes() {
        final var bat = new Migration();
        bat.name = "Bat";

        final var foundBat = migrations.findById(
                migrations.save(bat).id);
        assertThat(foundBat).contains(bat);

        final var austin = new Roost()
                .add(bat);
        austin.name = "Austin";

        final var spring = new Season();
        spring.name = "Spring";
        final var seasonBatRef = new SeasonMigrationRef();
        seasonBatRef.id = bat.id;
        spring.migrations.add(seasonBatRef);

        final var foundAustin = roosts.findById(
                roosts.save(austin).id);
        assertThat(foundAustin).contains(austin);

        final var foundSpring = seasons.findById(
                seasons.save(spring).id);
        assertThat(foundSpring).contains(spring);
    }
}
