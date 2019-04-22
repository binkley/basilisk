package hm.binkley.basilisk.x;

import hm.binkley.basilisk.x.kind.Kind;
import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.kind.store.KindRecord;
import hm.binkley.basilisk.x.middle.Middle;
import hm.binkley.basilisk.x.middle.Middles;
import hm.binkley.basilisk.x.middle.store.MiddleRecord;
import hm.binkley.basilisk.x.near.Near;
import hm.binkley.basilisk.x.near.Nears;
import hm.binkley.basilisk.x.near.store.NearRecord;
import hm.binkley.basilisk.x.near.store.NearRepository;
import hm.binkley.basilisk.x.near.store.NearStore;
import hm.binkley.basilisk.x.top.Top;
import hm.binkley.basilisk.x.top.store.TopRecord;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class DomainsTest {
    private static final String nearCode = "NER";
    private static final String middleCode = "MID";
    private static final String kindCode = "KIN";
    private static final String topCode = "TOP";
    private static final String topName = "TWIRL";

    @Test
    void shouldExistOrNot() {
        final var nearRepository = mock(NearRepository.class);
        final var nearStore = new NearStore(nearRepository);
        final var nears = new Nears(nearStore);

        assertThat(nears.exists("LALA")).isFalse();

        when(nearRepository.existsById(nearCode)).thenReturn(true);

        assertThat(nears.exists(nearCode)).isTrue();
    }

    @Test
    void shouldRollUpFromBottom() {
        final var nears = mock(Nears.class);
        final var near = nearOf(nearCode, nears);

        assertThat(near.getCode()).isEqualTo(nearCode);

        final var top = new Top(TopRecord.unsaved(topCode, topName),
                mock(Middles.class), nears);

        assertThat(top.getCode()).isEqualTo(topCode);

        final var kinds = mock(Kinds.class);
        final var kind = kindOf(kindCode, kinds, nears);

        assertThat(kind.getCode()).isEqualTo(kindCode);

        final var middle = new Middle(
                MiddleRecord.unsaved(middleCode, 222), kinds, nears)
                .attachToKind(kind);

        assertThat(middle.getCode()).isEqualTo(middleCode);
        assertThat(middle.getCoolness()).isEqualTo(kind.getCoolness());
    }

    @Test
    void shouldHaveNoNetNears() {
        final var middles = mock(Middles.class);
        final var nears = mock(Nears.class);
        final var top = new Top(TopRecord.unsaved(topCode, topName),
                middles, nears);

        assertThat(top.getEstimatedNears().map(Near::getCode)).isEmpty();
    }

    @Test
    void shouldIntersect() {
        final var nears = mock(Nears.class);
        final var nearA = nearOf(nearCode, nears);
        final var nearB = nearOf("FAR", nears);
        final var nearC = nearOf("CLO", nears);

        final var kinds = mock(Kinds.class);
        final Kind kind = kindOf(kindCode, kinds, nears);
        kind.addNear(nearA).addNear(nearB); // Kind knows of near A & B

        final var middles = mock(Middles.class);
        // Inherits nears from kind
        final Middle middleA = middleOf(middleCode, middles, nears, kinds);
        middleA.attachToKind(kind);
        // Overrides nears
        final Middle middleB = middleOf("CEN", middles, nears, kinds);
        middleB.addNear(nearA).addNear(nearC); // Middle B know of near A & C
        // Has no kind or overrides -- ignored
        final Middle middleC = middleOf("INN", middles, nears, kinds);

        final var top = new Top(TopRecord.unsaved(topCode, topName),
                middles, nears);
        top.addMiddle(middleA).addMiddle(middleB).addMiddle(middleC);

        assertThat(kind.getEstimatedNears().map(Near::getCode))
                .containsExactly(nearA.getCode(), nearB.getCode());
        assertThat(kind.getOthersNears()).isEmpty();
        assertThat(top.isPlanned()).isFalse();
        assertThat(top.getEstimatedNears().map(Near::getCode))
                .containsExactly(nearCode);
    }

    @Test
    void shouldPreferPlannedNearButShowEsitmated() {
        final var nears = mock(Nears.class);

        final var estimatedNear = nearOf("FAR", nears);
        final var plannedNear = nearOf(nearCode, nears);

        final var kinds = mock(Kinds.class);
        final var middles = mock(Middles.class);

        final var middle = middleOf(middleCode, middles, nears, kinds)
                .addNear(estimatedNear);

        final var top = new Top(TopRecord.unsaved(topCode, topName),
                middles, nears)
                .addMiddle(middle)
                .planWith(plannedNear);

        assertThat(top.isPlanned()).isTrue();
        assertThat(top.getEstimatedNears()).containsOnly(estimatedNear);
        assertThat(top.getPlannedNear()).contains(plannedNear);
    }

    private Near nearOf(final String nearCode, final Nears nears) {
        final var record = spy(NearRecord.unsaved(
                nearCode));
        doReturn(record).when(record).save();
        final var near = new Near(record);
        when(nears.byCode(record.code)).thenReturn(Optional.of(near));
        return near;
    }

    private Kind kindOf(final String kindCode, final Kinds kinds,
            final Nears nears) {
        final var record = spy(KindRecord.unsaved(
                kindCode, new BigDecimal("2.3")));
        doReturn(record).when(record).save();
        final var kind = new Kind(record, nears);
        when(kinds.byCode(record.code)).thenReturn(Optional.of(kind));
        return kind;
    }

    private Middle middleOf(final String middleCode, final Middles middles,
            final Nears nears, final Kinds kinds) {
        final var record = spy(MiddleRecord.unsaved(
                middleCode, 222));
        doReturn(record).when(record).save();
        final var middle = new Middle(record, kinds, nears);
        when(middles.byCode(record.code)).thenReturn(Optional.of(middle));
        return middle;
    }
}
