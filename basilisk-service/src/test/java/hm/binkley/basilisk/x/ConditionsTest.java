package hm.binkley.basilisk.x;

import hm.binkley.basilisk.x.kind.Kind;
import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.kind.store.KindRepository;
import hm.binkley.basilisk.x.kind.store.KindStore;
import hm.binkley.basilisk.x.middle.Bottom;
import hm.binkley.basilisk.x.middle.Middle;
import hm.binkley.basilisk.x.middle.Middles;
import hm.binkley.basilisk.x.middle.store.BottomRecord;
import hm.binkley.basilisk.x.middle.store.MiddleRecord;
import hm.binkley.basilisk.x.middle.store.MiddleRepository;
import hm.binkley.basilisk.x.middle.store.MiddleStore;
import hm.binkley.basilisk.x.near.Nears;
import hm.binkley.basilisk.x.near.store.NearRecord;
import hm.binkley.basilisk.x.near.store.NearRepository;
import hm.binkley.basilisk.x.near.store.NearStore;
import hm.binkley.basilisk.x.side.Side;
import hm.binkley.basilisk.x.side.Sides;
import hm.binkley.basilisk.x.side.store.SideRepository;
import hm.binkley.basilisk.x.side.store.SideStore;
import hm.binkley.basilisk.x.top.Top;
import hm.binkley.basilisk.x.top.Tops;
import hm.binkley.basilisk.x.top.store.TopRepository;
import hm.binkley.basilisk.x.top.store.TopStore;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
class ConditionsTest {
    private static final String nearCode = "NER";
    private static final String sideCode = "SID";
    private static final String kindCode = "KIN";
    private static final BigDecimal kindCoolness = new BigDecimal("2.3");
    private static final String middleCode = "MID";
    private static final int middleMid = 222;
    private static final String bottomFoo = "BAR";
    private static final String topCode = "TOP";
    private static final String topName = "TWIRL";

    @Mock
    private final NearRepository nearSpringData;
    @Mock
    private final SideRepository sideSpringData;
    @Mock
    private final TopRepository topSpringData;
    @Mock
    private final KindRepository kindSpringData;
    @Mock
    private final MiddleRepository middleSpringData;

    private Nears nears;
    private Sides sides;
    private Kinds kinds;
    private Middles middles;
    private Tops tops;

    private static Bottom newBottom() {
        return new Bottom(BottomRecord.unsaved(bottomFoo));
    }

    @BeforeEach
    void setUp() {
        nears = new Nears(new NearStore(nearSpringData));
        sides = new Sides(new SideStore(sideSpringData));
        kinds = new Kinds(new KindStore(kindSpringData), nears);
        middles = new Middles(new MiddleStore(middleSpringData),
                kinds, nears);
        tops = new Tops(new TopStore(topSpringData), middles, nears);
    }

    @Test
    void shouldComplainOnMissingNaturalKey() {
        assertThatThrownBy(() ->
                sides.unsaved(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() ->
                tops.unsaved(null, newSide(), topName))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() ->
                kinds.unsaved(null, kindCoolness))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() ->
                middles.unsaved(null, newSide(), middleMid))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() ->
                tops.unsaved(null, newSide(), topName))
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
        doAnswer(invocation -> invocation.getArgument(0))
                .when(nearSpringData).save(any(NearRecord.class));
        final var near = nears.unsaved(nearCode);
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
        doAnswer(invocation -> invocation.getArgument(0))
                .when(nearSpringData).save(any(NearRecord.class));
        final var near = nears.unsaved(nearCode);
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
        final var side = newSide();
        final var middle = side.applyInto(sideRecord ->
                MiddleRecord.unsaved(middleCode, sideRecord, middleMid, 0));
        final var bottom = BottomRecord.unsaved(bottomFoo);
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
        doAnswer(invocation -> invocation.getArgument(0))
                .when(middleSpringData).save(any(MiddleRecord.class));
        final var top = newTop();

        assertThatThrownBy(() -> top.removeMiddle(newMiddle()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void shouldComplainOnImpossibleUpdates() {
        assertThatThrownBy(() ->
                nears.unsavedSequenced(nearCode, -1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->
                sides.unsavedSequenced(sideCode, -1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->
                kinds.unsavedSequenced(kindCode, kindCoolness, -1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->
                middles.unsavedSequenced(middleCode, newSide(), middleMid,
                        -1))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() ->
                tops.unsavedSequenced(topCode, newSide(), topName, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Side newSide() {
        final var side = sides.unsaved(sideCode);
        side.applyInto(record ->
                lenient().when(sideSpringData.save(record))
                        .thenReturn(record));
        return side;
    }

    private Kind newKind() {
        return kinds.unsaved(kindCode, kindCoolness);
    }

    private Middle newMiddle() {
        return middles.unsaved(middleCode, newSide(), middleMid);
    }

    private Top newTop() {
        return tops.unsaved(topCode, newSide(), topName);
    }
}
