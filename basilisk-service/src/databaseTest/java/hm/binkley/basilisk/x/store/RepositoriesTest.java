package hm.binkley.basilisk.x.store;

import hm.binkley.basilisk.x.kind.Kind;
import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.kind.store.KindRecord;
import hm.binkley.basilisk.x.kind.store.KindRepository;
import hm.binkley.basilisk.x.kind.store.KindStore;
import hm.binkley.basilisk.x.middle.Bottom;
import hm.binkley.basilisk.x.middle.Middle;
import hm.binkley.basilisk.x.middle.Middles;
import hm.binkley.basilisk.x.middle.store.BottomRecord;
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
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("PMD")
@Transactional
class RepositoriesTest {
    private static final String bottomFoo = "BAR";
    private static final String sideCode = "SID";
    private static final Instant sideTime = Instant.ofEpochSecond(1_000_000);
    private static final String kindCode = "KIN";
    private static final BigDecimal kindCoolness = new BigDecimal("2.3");
    private static final String middleCode = "MID";
    private static final int middleMid = 222;
    private static final String topCode = "TOP";
    private static final String topName = "TWIRL";

    @Spy
    private final NearRepository nearRepository;
    @Spy
    private final TopRepository topRepository;
    @Spy
    private final KindRepository kindRepository;
    @Spy
    private final MiddleRepository middleRepository;
    @Spy
    private final SideRepository sideRepository;

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
        nears = new Nears(new NearStore(nearRepository));
        sides = new Sides(new SideStore(sideRepository));
        kinds = new Kinds(new KindStore(kindRepository), nears);
        middles = new Middles(new MiddleStore(middleRepository),
                kinds, sides, nears);
        tops = new Tops(new TopStore(topRepository), middles, sides, nears);
    }

    @Test
    void shouldCascadeSaveTopToBottom() {
        final var unsavedKindNear = nears.unsaved("KNR");
        final var unsavedMiddleNear = nears.unsaved("MNR");
        final var unsavedTopNear = nears.unsaved("TNR");
        final var unsavedSide = sides.unsaved(sideCode, sideTime);
        final var unsavedKind = kinds.unsaved(kindCode, kindCoolness)
                .addNear(unsavedKindNear);
        final var unsavedMiddle = middles.unsaved(middleCode, middleMid)
                .attachToKind(unsavedKind)
                .attachToSide(unsavedSide)
                .addBottom(bottomFoo)
                .addNear(unsavedMiddleNear);
        final var unsavedTop = tops.unsaved(
                topCode, topName, unsavedSide, false)
                .addMiddle(unsavedMiddle)
                .addNear(unsavedTopNear);

        final var savedTop = unsavedTop.save();
        final var readBackTop = tops.byCode(savedTop.getCode()).orElseThrow();

        assertThat(readBackTop).isEqualTo(unsavedTop);
        assertThat(readBackTop.getName()).isEqualTo(topName);
        final var readBackSide = readBackTop.getSide();
        assertThat(readBackSide).isEqualTo(unsavedSide);
        assertThat(readBackSide.getTime()).isEqualTo(sideTime);
        final var readBackMiddle = readBackTop.getMiddles()
                .findFirst()
                .orElseThrow();
        assertThat(readBackMiddle).isEqualTo(unsavedMiddle);
        assertThat(readBackMiddle.getSide()
                .orElseThrow()).isEqualTo(readBackSide);
        assertThat(readBackMiddle.getMid()).isEqualTo(middleMid);
        assertThat(readBackMiddle.getBottoms()
                .findFirst()
                .orElseThrow()
                .getFoo()).isEqualTo(bottomFoo);
        final var readBackKind = readBackMiddle.getKind().orElseThrow();
        assertThat(readBackKind).isEqualTo(unsavedKind);
        assertThat(readBackKind.getCoolness()).isEqualTo(kindCoolness);
        final var readBackKindNear = readBackKind.getOwnNears()
                .findFirst()
                .orElseThrow();
        assertThat(readBackKindNear).isEqualTo(unsavedKindNear);

        assertBottomCount(1);
        assertSideCount(1);
        assertMiddleCounts(1, 0);
        assertKindCount(1);
        assertTopCount(1);
    }

    @Test
    void shouldDelete() {
        final var top = newTop().save();

        assertTopCount(1);

        top.delete();

        assertTopCount(0);

        final var middle = newMiddle().save();

        assertMiddleCounts(0, 1);

        middle.delete();

        assertMiddleCounts(0, 0);

        final var side = newSide().save();

        assertSideCount(1);

        side.delete();

        assertSideCount(0);

        final var kind = newKind().save();

        assertKindCount(1);

        kind.delete();

        assertKindCount(0);

        final var near = nears.unsaved("NER").save();

        assertNearCount(1);

        near.delete();

        assertNearCount(0);
    }

    @Test
    void shouldSaveDateTimeTypes() {
        final var unsaved = newSide();

        final var saved = unsaved.save();

        assertThat(saved).isEqualTo(unsaved);
        assertSideCount(1);
    }

    @Test
    void shouldGracefullyCopeWithNoKind() {
        final var kind = newKind().save();
        final var middle = newMiddle()
                .attachToKind(kind).save();

        assertThat(middle.getKind().orElseThrow()).isEqualTo(kind);
        assertThat(middle.getCoolness()).isEqualTo(kind.getCoolness());

        middle.detachFromKind().save();
        kind.delete();

        assertThat(middle.getKind()).isEmpty();
        assertThat(middle.getCoolness()).isNull();
    }

    @Test
    void shouldGracefullyCopeWithNoSide() {
        final var side = newSide().save();
        final var middle = newMiddle()
                .attachToSide(side).save();

        assertThat(middle.getSide().orElseThrow()).isEqualTo(side);

        middle.detachFromSide().save();
        side.delete();

        assertThat(middle.getSide()).isEmpty();
    }

    @Test
    void shouldSaveOneToOneRelationshipsAtDefinition() {
        newMiddle().attachToKind(newKind());

        assertKindCount(1);
    }

    @Test
    void shouldSaveManyToOneRelationshipsAtReference() {
        final var top = newTop().addMiddle(newMiddle());

        assertMiddleCounts(0, 1);

        top.save();

        assertMiddleCounts(1, 0);
    }

    @Test
    void shouldAddValueObjectsBeforeSave() {
        newMiddle()
                .addBottom(newBottom())
                .save();

        assertBottomCount(1);
    }

    @Test
    void shouldAddValueObjectsAfterSave() {
        final var bottom = newBottom();
        newMiddle()
                .save()
                .addBottom(bottom)
                .save();

        assertThat(bottom.getFoo()).isEqualTo(bottomFoo);
        assertBottomCount(1);
    }

    @Test
    void shouldRemoveValueObjectsDirectly() {
        final var bottom = newBottom();
        final var middle = newMiddle()
                .addBottom(bottom)
                .save();

        assertBottomCount(1);

        middle.removeBottom(bottom);
        middle.save();

        assertBottomCount(0);
    }

    @Test
    void shouldRemoveValueObjectsOnDeleteOwner() {
        final var savedMiddle = newMiddle()
                .addBottom(newBottom())
                .save();
        final var savedCode = savedMiddle.getCode();

        assertBottomCount(1);

        savedMiddle.delete();

        assertBottomCount(0);

        assertThat(middles.byCode(savedCode)).isEmpty();
    }

    @Test
    void shouldNotRemoveAnotherEntityOnDeleteOwner() {
        final var top = newTop()
                .addMiddle(newMiddle())
                .save();

        assertMiddleCounts(1, 0);

        top.delete();

        assertMiddleCounts(0, 1);
    }

    @Test
    void shouldNotRemoveAnotherEntityOnRemoveReference() {
        final var middle = newMiddle();
        final var top = newTop()
                .addMiddle(middle)
                .save();

        assertMiddleCounts(1, 0);

        final var firstMiddle = top.getMiddles().findFirst().orElseThrow();
        assertThat(firstMiddle).isEqualTo(middle);
        assertThat(firstMiddle.getCode()).isEqualTo(middle.getCode());

        top.removeMiddle(middle);
        top.save();

        assertMiddleCounts(0, 1);
    }

    @Test
    void shouldRollupNearness() {
        final var kindNear = nears.unsaved("KNR");
        final var kind = newKind()
                .addNear(kindNear);
        final var middle = newMiddle()
                .attachToKind(kind);
        final var top = newTop()
                .addMiddle(middle)
                .save();

        assertThat(top.getNetNears()).containsExactly(kindNear);

        final var middleNear = nears.unsaved("MNR");
        middle.addNear(middleNear).save();

        assertThat(top.getNetNears()).containsExactly(middleNear);

        final var topNear = nears.unsaved("TNR");
        top.addNear(topNear).save();

        assertThat(top.getNetNears()).containsExactly(topNear);

        kind.removeNear(kindNear).save();

        assertThat(kind.getOwnNears()).isEmpty();
        assertThat(top.getNetNears()).containsExactly(topNear);

        middle.removeNear(middleNear).save();

        assertThat(middle.getOwnNears()).isEmpty();
        assertThat(top.getNetNears()).containsExactly(topNear);

        top.removeNear(topNear).save();

        assertThat(top.getOwnNears()).isEmpty();
        assertThat(top.getNetNears()).isEmpty();
    }

    @Test
    void shouldComplainOnDuplicateMiddle() {
        final var middle = newMiddle();
        final var top = newTop().addMiddle(middle);

        assertThatThrownBy(() -> top.addMiddle(middle))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void whereDoesThisTestGo() {
        final var kindRecord = KindRecord.unsaved(
                "KIN", new BigDecimal("2.3"));
        assertThat(kindRecord.hasNears()).isFalse();
        final var nearRecord = NearRecord.unsaved("NER");
        final var nearStore = mock(NearStore.class);
        when(nearStore.save(nearRecord))
                .thenReturn(nearRecord);
        nearRecord.store = nearStore;
        kindRecord.addNear(nearRecord);
        assertThat(kindRecord.hasNears()).isTrue();
    }

    private Side newSide() {
        return sides.unsaved(sideCode, sideTime);
    }

    private Middle newMiddle() {
        return middles.unsaved(middleCode, middleMid);
    }

    private Kind newKind() {
        return kinds.unsaved(kindCode, kindCoolness);
    }

    private Top newTop() {
        return tops.unsaved(topCode, topName, newSide(), false);
    }

    private void assertBottomCount(final int total) {
        assertThat(middleRepository.findAllBottoms()).hasSize(total);
    }

    private void assertSideCount(final int total) {
        assertThat(sides.all()).hasSize(total);
    }

    private void assertMiddleCounts(final int owned, final int free) {
        assertThat(middles.all()).hasSize(owned + free);
        assertThat(middles.owned()).hasSize(owned);
        assertThat(middles.free()).hasSize(free);
    }

    private void assertKindCount(final int total) {
        assertThat(kinds.all()).hasSize(total);
    }

    private void assertTopCount(final int total) {
        assertThat(tops.all()).hasSize(total);
    }

    private void assertNearCount(final int total) {
        assertThat(nears.all()).hasSize(total);
    }
}
