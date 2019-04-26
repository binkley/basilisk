package hm.binkley.basilisk.x.near.store;

import hm.binkley.basilisk.store.BasiliskDatabaseTest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@BasiliskDatabaseTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class NearStoreTest {
    private final NearRepository springData;

    private NearStore store;

    @BeforeEach
    void setUp() {
        store = new NearStore(springData);
    }

    @Test
    void shouldRoundTrip() {
        final var unsaved = store.unsaved("NER", 0);

        final var saved = unsaved.save();

        assertThat(saved).isEqualTo(unsaved);
    }

    @Test
    void shouldRejectStaleUpdates() {
        final var code = "NER";
        store.unsaved(code, 2).save();
        store.unsaved(code, 0).save();

        assertThatThrownBy(() ->
                store.unsaved(code, 1).save())
                .isInstanceOf(UncategorizedSQLException.class);
    }
}
