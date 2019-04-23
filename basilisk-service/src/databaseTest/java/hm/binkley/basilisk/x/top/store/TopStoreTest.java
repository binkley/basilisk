package hm.binkley.basilisk.x.top.store;

import hm.binkley.basilisk.x.side.store.SideRepository;
import hm.binkley.basilisk.x.side.store.SideStore;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class TopStoreTest {
    private final TopRepository springData;
    private final SideRepository sideSpringData;

    private TopStore store;
    private SideStore sideStore;

    @BeforeEach
    void setUp() {
        store = new TopStore(springData);
        sideStore = new SideStore(sideSpringData);
    }

    @Test
    void shouldRoundTrip() {
        final var sideCode = "SID";
        final var unsaved = store.unsaved(
                "MID", sideStore.unsaved(sideCode), "TWIRL");

        final var saved = unsaved.save();

        assertThat(saved).isEqualTo(unsaved);
    }
}
