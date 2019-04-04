package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
class ConditionsTest {
    @Mock
    private final SideRepository sideRepository;
    @Mock
    private final TopRepository topRepository;
    @Mock
    private final KindRepository kindRepository;
    @Mock
    private final MiddleRepository middleRepository;

    private Sides sides;
    private Kinds kinds;
    private Middles middles;
    private Tops tops;

    private static Bottom newBottom() {
        return new Bottom(BottomRecord.unsaved("BAR"));
    }

    @BeforeEach
    void setUp() {
        sides = new Sides(new SideStore(sideRepository));
        kinds = new Kinds(new KindStore(kindRepository));
        middles = new Middles(new MiddleStore(middleRepository),
                kinds, sides);
        tops = new Tops(new TopStore(topRepository), middles, sides);
    }

    @Test
    void shouldComplainOnMissingNaturalKey() {
        assertThatThrownBy(() ->
                sides.unsaved(null, Instant.ofEpochSecond(1_000_000)))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() ->
                tops.unsaved(null, "TWIRL", newSide()))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() ->
                kinds.unsaved(null, new BigDecimal("2.3")))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() ->
                middles.unsaved(null, 222))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnMissingSide() {
        final var middle = newMiddle();

        assertThatThrownBy(() -> tops.unsaved("TOP", "TWIRL", null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> middle.defineSide(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnMissingKind() {
        final var middle = newMiddle();

        assertThatThrownBy(() -> middle.defineKind(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnAbsentKind() {
        final var middle = newMiddle();

        assertThatThrownBy(middle::undefineKind)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnAbsentSide() {
        final var middle = newMiddle();

        assertThatThrownBy(middle::undefineSide)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnMissingBottom() {
        final var middle = newMiddle();

        assertThatThrownBy(() -> middle.add(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> middle.remove(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnDuplicateBottom() {
        final var middle = newMiddle().add(newBottom());

        assertThatThrownBy(() -> middle.add(newBottom()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnAbsentBottom() {
        final var middle = newMiddle();

        assertThatThrownBy(() -> middle.remove(newBottom()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldComplainOnMismatchedBottomAfterSave() {
        final var middle = MiddleRecord.unsaved("MID", 222);
        final var bottom = BottomRecord.unsaved("BAR");
        bottom.middleCode = middle.code + "-X";

        assertThatThrownBy(() -> middle.add(bottom))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnMissingMiddle() {
        final var top = newTop();

        assertThatThrownBy(() -> top.add(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> top.remove(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnAbsentMiddle() {
        final var top = newTop();

        assertThatThrownBy(() -> top.remove(newMiddle()))
                .isInstanceOf(NoSuchElementException.class);
    }

    private Side newSide() {
        return sides.unsaved("SID", Instant.ofEpochMilli(1_000_000));
    }

    private Middle newMiddle() {
        return middles.unsaved("MID", 222);
    }

    private Top newTop() {
        return tops.unsaved("TOP", "TWIRL", newSide());
    }
}
