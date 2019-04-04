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
        return new Bottom(BottomRecord.unsaved("BAR"));
    }

    @BeforeEach
    void setUp() {
        sides = new Sides(new SideStore(sideRepository));
        kinds = new Kinds(new KindStore(kindRepository));
        middles = new Middles(new MiddleStore(middleRepository), kinds);
        tops = new Tops(new TopStore(topRepository), middles);
    }

    @Test
    void shouldCascadeSaveTopToBottom() {
        final var mid = 222;
        final var unsavedMiddle = middles.unsaved("MID", mid);
        unsavedMiddle.addBottom("BAR");
        final var coolness = new BigDecimal("2.3");
        final var unsavedKind = kinds.unsaved("KIN", coolness);
        unsavedMiddle.define(unsavedKind);
        final var topName = "TWIRL";
        final var unsavedTop = tops.unsaved("TOP", topName);
        unsavedTop.add(unsavedMiddle);

        final var savedTop = unsavedTop.save();

        assertThat(savedTop.getName()).isEqualTo(topName);
        assertThat(unsavedMiddle.getMid()).isEqualTo(mid);
        assertThat(unsavedMiddle.getCoolness()).isEqualTo(coolness);
        assertBottomCount(1);
        assertSideCount(0);
        assertMiddleCounts(1, 0);
        assertKindCount(1);
        assertTopCount(1);

        assertThat(tops.byCode(savedTop.getCode()).orElseThrow())
                .isEqualTo(savedTop);
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
        final var middle = newMiddle();
        middle.define(kind).save();

        assertThat(middle.getKind().orElseThrow()).isEqualTo(kind);
        assertThat(middle.getCoolness()).isEqualTo(kind.getCoolness());

        kind.delete();

        assertThat(middle.getKind()).isEmpty();
        assertThat(middle.getCoolness()).isNull();
    }

    @Test
    void shouldSaveOneToOneRelationshipsAtDefinition() {
        newMiddle().define(newKind());

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

        assertThat(bottom.getFoo()).isEqualTo("BAR");
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
        return sides.unsaved("SID", Instant.ofEpochSecond(1_000_000));
    }

    private Middle newMiddle() {
        return middles.unsaved("MID", 222);
    }

    private Kind newKind() {
        return kinds.unsaved("KIN", new BigDecimal("2.3"));
    }

    private Top newTop() {
        return tops.unsaved("TOP", "TWIRL");
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
