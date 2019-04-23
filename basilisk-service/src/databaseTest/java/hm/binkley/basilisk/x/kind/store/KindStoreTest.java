package hm.binkley.basilisk.x.kind.store;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
class KindStoreTest {
    private final KindRepository springData;

    private KindStore store;

    @BeforeEach
    void setUp() {
        store = new KindStore(springData);
    }

    @Test
    void shouldRoundTrip() {
        final var unsaved = store.unsaved("KID", new BigDecimal("2.3"), 0);

        final var saved = unsaved.save();

        assertThat(saved).isEqualTo(unsaved);
    }

    @Test
    void shouldRejectStaleUpdates() {
        final var code = "NER";
        final var coolness = new BigDecimal("2.3");
        store.unsaved(code, coolness, 2).save();
        store.unsaved(code, coolness, 0).save();

        assertThatThrownBy(() ->
                store.unsaved(code, coolness, 1).save())
                .isInstanceOf(UncategorizedSQLException.class);
    }
}
