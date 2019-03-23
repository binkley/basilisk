package hm.binkley.basilisk.store.x;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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
    private final MiddleRepository middleRepository;

    private static BottomRecord newBottom() {
        return BottomRecord.unsaved("BAR");
    }

    @Test
    void shouldCascadeSaveTopToBottom() {
        final var middle = newMiddle()
                .add(newBottom());
        newTop().add(middle).save();

        assertBottomCount(1);
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
                .save()
                .refresh();

        assertBottomCount(1);

        middle.remove(bottom);
        middle.save();

        assertBottomCount(0);
    }

    @Test
    void shouldRemoveValueObjectsOnDeleteOwner() {
        final var middle = newMiddle()
                .add(newBottom())
                .save()
                .refresh();

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
        final var middle = newMiddle()
                .add(newBottom())
                .save()
                .refresh();

        assertMiddleCounts(0, 1);

        final var top = newTop()
                .add(middle)
                .save()
                .refresh();

        assertMiddleCounts(1, 0);

        top.delete();

        assertMiddleCounts(0, 1);
    }

    @Test
    void shouldNotRemoveAnotherEntityOnRemoveReference() {
        var middle = newMiddle().add(newBottom());
        middle = middle.save().refresh();

        assertMiddleCounts(0, 1);

        final var top = newTop()
                .add(middle)
                .save()
                .refresh();

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
