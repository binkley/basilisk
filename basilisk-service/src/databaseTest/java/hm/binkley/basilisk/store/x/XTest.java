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
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("PMD")
@Transactional
class XTest {
    @Spy
    private final TopRepository topRepository;
    @Spy
    private final KindRepository kindRepository;
    @Spy
    private final MiddleRepository middleRepository;

    private Kinds kinds;
    private Middles middles;
    private Tops tops;

    private static Bottom newBottom() {
        return new Bottom(BottomRecord.unsaved("BAR"));
    }

    @BeforeEach
    void setUp() {
        kinds = new Kinds(new KindStore(kindRepository));
        middles = new Middles(new MiddleStore(middleRepository), kinds);
        tops = new Tops(new TopStore(topRepository), middles);
    }

    @Test
    void shouldCascadeSaveTopToBottom() {
        final var mid = 222;
        final var unsavedMiddle = middles.unsaved(mid);
        unsavedMiddle.addBottom("BAR");
        final var coolness = new BigDecimal("2.3");
        final var unsavedKind = kinds.unsaved(coolness);
        unsavedMiddle.define(unsavedKind);
        final var topName = "TWIRL";
        final var unsavedTop = tops.unsaved(topName);
        unsavedTop.add(unsavedMiddle);

        final var savedTop = unsavedTop.save();

        assertThat(savedTop.getName()).isEqualTo(topName);
        assertThat(unsavedMiddle.getMid()).isEqualTo(mid);
        assertThat(unsavedMiddle.getCoolness()).isEqualTo(coolness);
        assertBottomCount(1);
        assertMiddleCounts(1, 0);
        assertKindCount(1);

        assertThat(tops.byId(savedTop.getId()).orElseThrow())
                .isEqualTo(savedTop);
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
        final var savedId = savedMiddle.getId();

        assertBottomCount(1);

        savedMiddle.delete();

        assertBottomCount(0);

        assertThat(middles.byId(savedId)).isEmpty();
    }

    @Test
    void shouldNotResaveAlreadySavedEntity() {
        final var middle = MiddleRecord.unsaved(222);
        middleRepository.save(middle);
        topRepository.save(TopRecord.unsaved("TWIRL").add(middle));

        assertMiddleCounts(1, 0);

        verify(middleRepository).save(middle); // default - times(1)
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
        assertThat(firstMiddle.getId()).isEqualTo(middle.getId());

        top.remove(middle);
        top.save();

        assertMiddleCounts(0, 1);
    }

    @Test
    void shouldResaveDeletedEntities() {
        final var savedTop = newTop().save();
        final var savedTopId = savedTop.getId();
        final var savedKind = newKind().save();
        final var savedKindId = savedKind.getId();
        final var savedMiddle = newMiddle().save();
        final var savedMiddleId = savedMiddle.getId();

        assertTopCount(1);
        assertKindCount(1);
        assertMiddleCounts(0, 1);

        final var resavedTop = savedTop.delete().save();
        final var resavedKind = savedKind.delete().save();
        final var resavedMiddle = savedMiddle.delete().save();

        assertTopCount(1);
        assertKindCount(1);
        assertMiddleCounts(0, 1);

        assertThat(resavedTop.getId()).isNotEqualTo(savedTopId);
        assertThat(resavedKind.getId()).isNotEqualTo(savedKindId);
        assertThat(resavedMiddle.getId()).isNotEqualTo(savedMiddleId);
    }

    @Test
    void shouldComplainOnUsingDeletedEntities() {
        final var deletedTop = newTop().save().delete();
        final var deletedKind = newKind().save().delete();
        final var deletedMiddle = newMiddle().save().delete();

        assertTopCount(0);
        assertKindCount(0);
        assertMiddleCounts(0, 0);

        assertThatThrownBy(() -> tops.byId(deletedTop.getId()))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> kinds.byId(deletedKind.getId()))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> middles.byId(deletedMiddle.getId()))
                .isInstanceOf(NullPointerException.class);
    }

    @SuppressWarnings("ConstantConditions")
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
    void shouldComplainOnMismatchedBottomBeforeSave() {
        final var middle = MiddleRecord.unsaved(222);
        final var bottom = BottomRecord.unsaved("BAR");
        bottom.middleId = 1L;

        assertThatThrownBy(() -> middle.add(bottom))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnMismatchedBottomAfterSave() {
        final var middle = MiddleRecord.unsaved(222);
        middleRepository.save(middle);
        final var bottom = BottomRecord.unsaved("BAR");
        bottom.middleId = middle.id + 1;

        assertThatThrownBy(() -> middle.add(bottom))
                .isInstanceOf(IllegalStateException.class);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void shouldComplainOnMissingMiddle() {
        final var top = newTop();

        assertThatThrownBy(() -> top.add(null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> top.remove(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnDuplicateMiddle() {
        final var middle = newMiddle();
        final var top = newTop().add(middle);

        assertThatThrownBy(() -> top.add(middle))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnAbsentMiddle() {
        final var top = newTop();

        assertThatThrownBy(() -> top.remove(newMiddle()))
                .isInstanceOf(NoSuchElementException.class);
    }

    private Middle newMiddle() {
        return middles.unsaved(222);
    }

    private Kind newKind() {
        return kinds.unsaved(new BigDecimal("2.3"));
    }

    private Top newTop() {
        return tops.unsaved("TWIRL");
    }

    private void assertBottomCount(final int total) {
        assertThat(middleRepository.findAllBottoms()).hasSize(total);
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
