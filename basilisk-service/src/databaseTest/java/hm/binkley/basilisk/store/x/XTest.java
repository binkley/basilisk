package hm.binkley.basilisk.store.x;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("PMD")
@Transactional
class XTest {
    private final TopRepository topRepository;
    private final MiddleRepository middleRepository;

    private static BottomRecord newBottom() {
        return BottomRecord.unsaved("BAR");
    }

    @Test
    void shouldAddNonEntitiesAfterSave() {
        var middle = newMiddle().save();
        final var bottom = newBottom();
        middle.add(bottom);
        middle = middle.save();

        assertBottomCount(1);

        middle.remove(bottom);
        middle.save();

        assertBottomCount(0);
    }

    @Test
    void shouldRemoveNonEntitiesDirectly() {
        final var bottom = newBottom();
        var middle = newMiddle().add(bottom);
        middle = middle.save().refresh();

        assertBottomCount(1);

        middle.remove(bottom);
        middle.save();

        assertBottomCount(0);
    }

    @Test
    void shouldRemoveNonEntitiesOnDeleteOwner() {
        var middle = newMiddle().add(newBottom());
        middle = middle.save().refresh();

        assertBottomCount(1);

        middle.delete();

        assertBottomCount(0);
    }

    @Test
    void shouldNotRemoveAnotherEntityOnDeleteOwner() {
        var middle = newMiddle().add(newBottom());
        middle = middle.save().refresh();

        assertMiddleCounts(0, 1);

        var top = newTop();
        top.add(middle);
        top = top.save().refresh();

        assertMiddleCounts(1, 0);

        top.delete();

        assertMiddleCounts(0, 1);
    }

    @Test
    void shouldNotRemoveAnotherEntityOnRemoveReference() {
        var middle = newMiddle().add(newBottom());
        middle = middle.save().refresh();

        assertMiddleCounts(0, 1);

        var top = newTop();
        top.add(middle);
        top = top.save().refresh();

        assertMiddleCounts(1, 0);

        top.remove(middle);
        top.save();

        assertMiddleCounts(0, 1);
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
        bottom.middle_id = 1L;

        assertThatThrownBy(() -> middle.add(bottom))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnMismatchedBottomAfterSave() {
        final var middle = newMiddle().save();
        final var bottom = newBottom();
        bottom.middle_id = middle.id + 1;

        assertThatThrownBy(() -> middle.add(bottom))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnDuplicateMiddle() {
        final var top = newTop().add(newMiddle());

        assertThatThrownBy(() -> top.add(newMiddle()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnAbsentMiddle() {
        final var top = newTop();

        assertThatThrownBy(() -> top.remove(newMiddle()))
                .isInstanceOf(IllegalStateException.class);
    }

    private MiddleRecord newMiddle() {
        return MiddleRecord.unsaved(222, middleRepository);
    }

    private TopRecord newTop() {
        return TopRecord.unsaved("TWIRL", topRepository);
    }

    private void assertBottomCount(final int total) {
        assertThat(middleRepository.findAllBottoms()).hasSize(total);
    }

    private void assertMiddleCounts(final int owned, final int free) {
        assertThat(middleRepository.findAll()).hasSize(owned + free);
        assertThat(middleRepository.findAllOwned()).hasSize(owned);
        assertThat(middleRepository.findAllFree()).hasSize(free);
    }
}
