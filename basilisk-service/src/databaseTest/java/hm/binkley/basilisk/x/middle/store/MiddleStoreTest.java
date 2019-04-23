package hm.binkley.basilisk.x.middle.store;

import hm.binkley.basilisk.x.side.store.SideRepository;
import hm.binkley.basilisk.x.side.store.SideStore;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class MiddleStoreTest {
    private final MiddleRepository springData;
    private final SideRepository sideSpringData;

    private MiddleStore store;
    private SideStore sideStore;

    @BeforeEach
    void setUp() {
        store = new MiddleStore(springData);
        sideStore = new SideStore(sideSpringData);
    }

    @Test
    void shouldRoundTrip() {
        final var sideCode = "SID";
        final var unsaved = store.unsaved(
                "MID", sideStore.unsaved(sideCode, 0), 222, 0);

        final var saved = unsaved.save();

        assertThat(saved).isEqualTo(unsaved);
    }

    @Test
    void shouldRejectStaleUpdates() {
        final var code = "SID";
        final var side = sideStore.unsaved("SID", 0);
        final var mid = 222;
        store.unsaved(code, side, mid, 2).save();
        store.unsaved(code, side, mid, 0).save();

        assertThatThrownBy(() ->
                store.unsaved(code, side, mid, 1).save())
                .isInstanceOf(UncategorizedSQLException.class);
    }
}
