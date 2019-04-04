package hm.binkley.basilisk.store.x;

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
    private final TopRepository topRepository;
    @Spy
    private final KindRepository kindRepository;
    @Spy
    private final MiddleRepository middleRepository;
    @Spy
    private final SideRepository sideRepository;

    private Sides sides;
    private Kinds kinds;
    private Middles middles;
    private Tops tops;

    private static Bottom newBottom() {
        return new Bottom(BottomRecord.unsaved(bottomFoo));
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
    void shouldCascadeSaveTopToBottom() {
        final var unsavedSide = sides.unsaved(sideCode, sideTime);
        final var unsavedKind = kinds.unsaved(kindCode, kindCoolness);
        final var unsavedMiddle = middles.unsaved(middleCode, middleMid)
                .defineKind(unsavedKind)
                .defineSide(unsavedSide)
                .addBottom(bottomFoo);
        final var unsavedTop = tops.unsaved(topCode, topName, unsavedSide)
                .add(unsavedMiddle);

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

        assertBottomCount(1);
        assertSideCount(1);
        assertMiddleCounts(1, 0);
        assertKindCount(1);
        assertTopCount(1);
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
                .defineKind(kind).save();

        assertThat(middle.getKind().orElseThrow()).isEqualTo(kind);
        assertThat(middle.getCoolness()).isEqualTo(kind.getCoolness());

        middle.undefineKind().save();
        kind.delete();

        assertThat(middle.getKind()).isEmpty();
        assertThat(middle.getCoolness()).isNull();
    }

    @Test
    void shouldGracefullyCopeWithNoSide() {
        final var side = newSide().save();
        final var middle = newMiddle()
                .defineSide(side).save();

        assertThat(middle.getSide().orElseThrow()).isEqualTo(side);

        middle.undefineSide().save();
        side.delete();

        assertThat(middle.getSide()).isEmpty();
    }

    @Test
    void shouldSaveOneToOneRelationshipsAtDefinition() {
        newMiddle().defineKind(newKind());

        assertKindCount(1);
    }

    @Test
    void shouldSaveManyToOneRelationshipsAtReference() {
        final var top = newTop().add(newMiddle());

        assertMiddleCounts(0, 1);

        top.save();

        assertMiddleCounts(1, 0);
    }

    @Test
    void shouldAddValueObjectsBeforeSave() {
        newMiddle()
                .add(newBottom())
                .save();

        assertBottomCount(1);
    }

    @Test
    void shouldAddValueObjectsAfterSave() {
        final var bottom = newBottom();
        newMiddle()
                .save()
                .add(bottom)
                .save();

        assertThat(bottom.getFoo()).isEqualTo(bottomFoo);
        assertBottomCount(1);
    }

    @Test
    void shouldRemoveValueObjectsDirectly() {
        final var bottom = newBottom();
        final var middle = newMiddle()
                .add(bottom)
                .save();

        assertBottomCount(1);

        middle.remove(bottom);
        middle.save();

        assertBottomCount(0);
    }

    @Test
    void shouldRemoveValueObjectsOnDeleteOwner() {
        final var savedMiddle = newMiddle()
                .add(newBottom())
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
                .add(newMiddle())
                .save();

        assertMiddleCounts(1, 0);

        top.delete();

        assertMiddleCounts(0, 1);
    }

    @Test
    void shouldNotRemoveAnotherEntityOnRemoveReference() {
        final var middle = newMiddle();
        final var top = newTop()
                .add(middle)
                .save();

        assertMiddleCounts(1, 0);

        final var firstMiddle = top.getMiddles().findFirst().orElseThrow();
        assertThat(firstMiddle).isEqualTo(middle);
        assertThat(firstMiddle.getCode()).isEqualTo(middle.getCode());

        top.remove(middle);
        top.save();

        assertMiddleCounts(0, 1);
    }

    @Test
    void shouldComplainOnDuplicateMiddle() {
        final var middle = newMiddle();
        final var top = newTop().add(middle);

        assertThatThrownBy(() -> top.add(middle))
                .isInstanceOf(IllegalStateException.class);
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
        return tops.unsaved(topCode, topName, newSide());
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
}
