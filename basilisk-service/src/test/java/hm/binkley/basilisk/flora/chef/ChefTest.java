package hm.binkley.basilisk.flora.chef;

import hm.binkley.basilisk.flora.chef.store.ChefRecord;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.savedChefRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedChefRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

class ChefTest {
    private static Answer<ChefRecord> simulateStore(final Long newId) {
        return invocation -> {
            final ChefRecord record = (ChefRecord) invocation.getMock();
            record.id = newId;
            return record;
        };
    }

    @Test
    void shouldSave() {
        final var record = spy(unsavedChefRecord());
        doAnswer(simulateStore(CHEF_ID))
                .when(record).save();

        final var saved = new Chef(record).save();

        assertThat(saved.getId()).isEqualTo(CHEF_ID);
    }

    @Test
    void shouldDelete() {
        final var record = spy(savedChefRecord());
        doAnswer(simulateStore(null))
                .when(record).delete();

        final var saved = new Chef(record).delete();

        assertThat(saved.getId()).isNull();
    }
}
