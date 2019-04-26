package hm.binkley.basilisk.x.top.store;

import hm.binkley.basilisk.store.BasiliskDatabaseTest;
import hm.binkley.basilisk.x.side.store.SideRepository;
import hm.binkley.basilisk.x.side.store.SideStore;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@BasiliskDatabaseTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
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
        final var unsaved = store.unsaved(
                "MID", sideStore.unsaved("SID", 0), "TWIRL", 0);

        final var saved = unsaved.save();

        assertThat(saved).isEqualTo(unsaved);
    }

    @Test
    void shouldRejectStaleUpdates() {
        final var code = "TOP";
        final var side = sideStore.unsaved("SID", 0);
        final var name = "TWIRL";
        store.unsaved(code, side, name, 2).save();
        store.unsaved(code, side, name, 0).save();

        assertThatThrownBy(() ->
                store.unsaved(code, side, name, 1).save())
                .isInstanceOf(UncategorizedSQLException.class);
    }
}
