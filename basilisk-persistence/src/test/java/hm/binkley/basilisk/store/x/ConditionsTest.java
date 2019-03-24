package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
class ConditionsTest {
    @Mock
    private final TopRepository topRepository;
    @Mock
    private final KindRepository kindRepository;
    @Mock
    private final MiddleRepository middleRepository;

    private Middles middles;
    private Tops tops;

    private static Bottom newBottom() {
        return new Bottom(BottomRecord.unsaved("BAR"));
    }

    @BeforeEach
    void setUp() {
        middles = new Middles(new MiddleStore(middleRepository),
                new Kinds(new KindStore(kindRepository)));
        tops = new Tops(new TopStore(topRepository), middles);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    void shouldComplainOnMissingKind() {
        final var middle = newMiddle();

        assertThatThrownBy(() -> middle.define(null))
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
        middle.id = null;
        final var bottom = BottomRecord.unsaved("BAR");
        bottom.middleId = 2L;

        assertThatThrownBy(() -> middle.add(bottom))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnMismatchedBottomAfterSave() {
        final var middle = MiddleRecord.unsaved(222);
        middle.id = 1L;
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

    private Middle newMiddle() {
        return middles.unsaved(222);
    }

    private Top newTop() {
        return tops.unsaved("TWIRL");
    }
}
