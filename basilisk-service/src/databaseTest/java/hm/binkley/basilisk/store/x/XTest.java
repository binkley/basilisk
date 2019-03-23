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

    private KindStore kindStore;
    private Kinds kinds;
    private MiddleStore middleStore;
    private Middles middles;

    private static <T> T first(final Iterable<T> c) {
        final var it = c.iterator();
        if (it.hasNext())
            return it.next();
        throw new NoSuchElementException();
    }

    private static BottomRecord newBottom() {
        return BottomRecord.unsaved("BAR");
    }

    @BeforeEach
    void setUp() {
        kindStore = new KindStore(kindRepository);
        kinds = new Kinds(kindStore);
        middleStore = new MiddleStore(middleRepository);
        middles = new Middles(middleStore, kinds);
    }

    @Test
    void shouldCascadeSaveTopToBottom() {
        final var unsaved = newTop()
                .add(newMiddle()
                        .define(newKind())
                        .add(newBottom()));
        final var saved = unsaved.save();

        assertBottomCount(1);
        assertMiddleCounts(1, 0);

        assertThat(saved).isEqualTo(unsaved);
        assertThat(saved.id).isEqualTo(unsaved.id);
        assertThat(first(saved.middles))
                .isEqualTo(first(unsaved.middles));
        assertThat(first(saved.middles).middleId)
                .isEqualTo(first(unsaved.middles).middleId);

        final var foundMiddle = middleStore
                .byId(first(saved.middles).middleId)
                .orElseThrow();

        assertThat(first(saved.middles).middleId)
                .isEqualTo(foundMiddle.id);

        final var refreshed = saved.refresh();
        final var refreshedMiddle = foundMiddle.refresh();

        assertThat(refreshed).isEqualTo(saved);
        assertThat(refreshed.id).isEqualTo(saved.id);
        assertThat(first(refreshed.middles))
                .isEqualTo(first(saved.middles));
        assertThat(first(refreshed.middles).middleId)
                .isEqualTo(first(saved.middles).middleId);

        assertThat(first(refreshed.middles).middleId)
                .isEqualTo(refreshedMiddle.id);
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
        newMiddle()
                .save()
                .add(newBottom())
                .save();

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
        final var middle = newMiddle()
                .add(newBottom())
                .save();

        assertBottomCount(1);

        middle.delete();

        assertBottomCount(0);
    }

    @Test
    void shouldNotResaveAlreadySavedEntity() {
        final var middle = newMiddle()
                .save();
        newTop().add(middle).save();

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

        top.remove(middle);
        top.save();

        assertMiddleCounts(0, 1);
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
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnMismatchedBottomBeforeSave() {
        final var middle = newMiddle();
        final var bottom = newBottom();
        bottom.middleId = 1L;

        assertThatThrownBy(() -> middle.add(bottom))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnMismatchedBottomAfterSave() {
        final var middle = newMiddle().save();
        final var bottom = newBottom();
        bottom.middleId = middle.id + 1;

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
                .isInstanceOf(IllegalStateException.class);
    }

    private MiddleRecord newMiddle() {
        return middleStore.unsaved(222);
    }

    private KindRecord newKind() {
        return kindStore.unsaved(new BigDecimal("2.3"));
    }

    private TopRecord newTop() {
        return TopRecord.unsaved("TWIRL", topRepository);
    }

    private void assertBottomCount(final int total) {
        assertThat(middleStore.allBottoms()).hasSize(total);
    }

    private void assertMiddleCounts(final int owned, final int free) {
        assertThat(middles.all()).hasSize(owned + free);
        assertThat(middles.owned()).hasSize(owned);
        assertThat(middles.free()).hasSize(free);
    }
}
