package hm.binkley.basilisk.flora.chef;

import org.junit.jupiter.api.Test;

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_RECIEVED_AT;
import static hm.binkley.basilisk.flora.FloraFixtures.savedChefRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedChefRecord;
import static hm.binkley.basilisk.store.PersistenceTesting.simulateRecordDelete;
import static hm.binkley.basilisk.store.PersistenceTesting.simulateRecordSave;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

class ChefTest {
    @Test
    void shouldSave() {
        final var record = spy(unsavedChefRecord());
        doAnswer(simulateRecordSave(CHEF_ID, CHEF_RECIEVED_AT))
                .when(record).save();

        final var saved = new Chef(record).save();

        assertThat(saved.getId()).isEqualTo(CHEF_ID);
    }

    @Test
    void shouldDelete() {
        final var record = spy(savedChefRecord());
        doAnswer(simulateRecordDelete())
                .when(record).delete();

        final var saved = new Chef(record).delete();

        assertThat(saved.getId()).isNull();
    }
}
