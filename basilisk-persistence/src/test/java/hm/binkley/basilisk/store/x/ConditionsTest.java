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
    private final NearRepository nearRepository;
    @Mock
    private final SideRepository sideRepository;
    @Mock
    private final TopRepository topRepository;
    @Mock
    private final KindRepository kindRepository;
    @Mock
    private final MiddleRepository middleRepository;

    private Nears nears;
    private Sides sides;
    private Kinds kinds;
    private Middles middles;
    private Tops tops;

    private static Bottom newBottom() {
        return new Bottom(BottomRecord.unsaved("BAR"));
    }

    @BeforeEach
    void setUp() {
        nears = new Nears(new NearStore(nearRepository));
        sides = new Sides(new SideStore(sideRepository));
        kinds = new Kinds(new KindStore(kindRepository), nears);
        middles = new Middles(new MiddleStore(middleRepository),
                kinds, sides, nears);
        tops = new Tops(new TopStore(topRepository), middles, sides, nears);
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
    void shouldComplainOnMissingNear() {
        final var top = newTop();
        final var middle = newMiddle();
        final var kind = newKind();

        assertThatThrownBy(() -> top.addNear(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> middle.addNear(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> kind.addNear(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnDuplicateNear() {
        final var near = nears.unsaved("NER");
        final var top = newTop().addNear(near);
        final var middle = newMiddle().addNear(near);
        final var kind = newKind().addNear(near);

        assertThatThrownBy(() -> top.addNear(near))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> middle.addNear(near))
                .isInstanceOf(IllegalStateException.class);
        assertThatThrownBy(() -> kind.addNear(near))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnAbsentNear() {
        final var near = nears.unsaved("NER");
        final var top = newTop();
        final var middle = newMiddle();
        final var kind = newKind();

        assertThatThrownBy(() -> top.removeNear(near))
                .isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(() -> middle.removeNear(near))
                .isInstanceOf(NoSuchElementException.class);
        assertThatThrownBy(() -> kind.removeNear(near))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldComplainOnMissingSide() {
        final var middle = newMiddle();

        assertThatThrownBy(() -> tops.unsaved("TOP", "TWIRL", null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> middle.attachToSide(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnMissingKind() {
        final var middle = newMiddle();

        assertThatThrownBy(() -> middle.attachToKind(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnAbsentKind() {
        final var middle = newMiddle();

        assertThatThrownBy(middle::detachFromKind)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnAbsentSide() {
        final var middle = newMiddle();

        assertThatThrownBy(middle::detachFromSide)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnMissingBottom() {
        final var middle = newMiddle();

        assertThatThrownBy(() -> middle.addNear(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> middle.removeNear(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnDuplicateBottom() {
        final var middle = newMiddle().addBottom(newBottom());

        assertThatThrownBy(() -> middle.addBottom(newBottom()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnAbsentBottom() {
        final var middle = newMiddle();

        assertThatThrownBy(() -> middle.removeBottom(newBottom()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldComplainOnMismatchedBottomAfterSave() {
        final var middle = MiddleRecord.unsaved("MID", 222);
        final var bottom = BottomRecord.unsaved("BAR");
        bottom.middleCode = middle.code + "-X";

        assertThatThrownBy(() -> middle.addBottom(bottom))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnMissingMiddle() {
        final var top = newTop();

        assertThatThrownBy(() -> top.addNear(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> top.removeNear(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnAbsentMiddle() {
        final var top = newTop();

        assertThatThrownBy(() -> top.removeMiddle(newMiddle()))
                .isInstanceOf(NoSuchElementException.class);
    }

    private Side newSide() {
        return sides.unsaved("SID", Instant.ofEpochMilli(1_000_000));
    }

    private Kind newKind() {
        return kinds.unsaved("KIN", new BigDecimal("2.3"));
    }

    private Middle newMiddle() {
        return middles.unsaved("MID", 222);
    }

    private Top newTop() {
        return tops.unsaved("TOP", "TWIRL", newSide());
    }
}
