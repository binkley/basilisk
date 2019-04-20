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
        final var nearRecord = spy(NearRecord.unsaved(nearCode));
        doReturn(nearRecord).when(nearRecord).save();
        final var near = new Near(nearRecord);
        final var nears = mock(Nears.class);
        when(nears.byCode(nearRecord.code)).thenReturn(Optional.of(near));

        assertThat(near.getCode()).isEqualTo(nearCode);

        final var topCode = DomainsTest.topCode;
        final var top = new Top(TopRecord.unsaved(topCode, topName),
                mock(Middles.class), nears);

        assertThat(top.getCode()).isEqualTo(topCode);

        final var kindRecord = spy(KindRecord.unsaved(
                kindCode, new BigDecimal("2.3")));
        doReturn(kindRecord).when(kindRecord).save();
        final var kind = new Kind(kindRecord, nears);
        final var kinds = mock(Kinds.class);
        when(kinds.byCode(kindRecord.code)).thenReturn(Optional.of(kind));

        assertThat(kind.getCode()).isEqualTo(kindCode);

        final var middle = new Middle(
                MiddleRecord.unsaved(middleCode, 222), kinds, nears)
                .attachToKind(kind);

        assertThat(middle.getCode()).isEqualTo(middleCode);
        assertThat(middle.getCoolness()).isEqualTo(kindRecord.coolness);
    }

    @Test
    void shouldHaveNoNetNears() {
        final var topCode = DomainsTest.topCode;
        final var middles = mock(Middles.class);
        final var nears = mock(Nears.class);
        final var top = new Top(TopRecord.unsaved(
                topCode, topName),
                middles, nears);

        assertThat(top.getPlannedNears().map(Near::getCode)).isEmpty();
    }

    @Test
    void shouldIntersect() {
        final var nearCodeA = nearCode;
        final var nearRecordA = spy(NearRecord.unsaved(nearCodeA));
        doReturn(nearRecordA).when(nearRecordA).save();
        final var nearA = new Near(nearRecordA);
        final var nears = mock(Nears.class);
        when(nears.byCode(nearRecordA.code)).thenReturn(Optional.of(nearA));
        final var nearRecordB = spy(NearRecord.unsaved("FAR"));
        doReturn(nearRecordB).when(nearRecordB).save();
        final var nearB = new Near(nearRecordB);
        when(nears.byCode(nearRecordB.code)).thenReturn(Optional.of(nearB));
        final var nearRecordC = spy(NearRecord.unsaved("CLO"));
        doReturn(nearRecordC).when(nearRecordC).save();
        final var nearC = new Near(nearRecordC);
        when(nears.byCode(nearRecordC.code)).thenReturn(Optional.of(nearC));

        final var kindRecordA = spy(KindRecord.unsaved(
                kindCode, new BigDecimal("2.3")));
        doReturn(kindRecordA).when(kindRecordA).save();
        final var kindA = new Kind(kindRecordA, nears);
        kindA.addNear(nearA).addNear(nearB);
        final var kinds = mock(Kinds.class);
        when(kinds.byCode(kindRecordA.code)).thenReturn(Optional.of(kindA));

        // Inherits nears from kind
        final var middleRecordA = spy(MiddleRecord.unsaved(middleCode, 222));
        doReturn(middleRecordA).when(middleRecordA).save();
        final var middleA = new Middle(middleRecordA, kinds, nears);
        middleA.attachToKind(kindA);
        final var middles = mock(Middles.class);
        when(middles.byCode(middleRecordA.code))
                .thenReturn(Optional.of(middleA));
        // Overrides nears
        final var middleRecordB = spy(MiddleRecord.unsaved("CEN", 222));
        doReturn(middleRecordB).when(middleRecordB).save();
        final var middleB = new Middle(middleRecordB, kinds, nears);
        middleB.addNear(nearA).addNear(nearC);
        when(middles.byCode(middleRecordB.code))
                .thenReturn(Optional.of(middleB));
        // Has no kind or overrides -- ignored
        final var middleRecordC = spy(MiddleRecord.unsaved("INN", 222));
        doReturn(middleRecordC).when(middleRecordC).save();
        final var middleC = new Middle(middleRecordC, kinds, nears);
        when(middles.byCode(middleRecordC.code))
                .thenReturn(Optional.of(middleC));

        final var topCode = DomainsTest.topCode;
        final var top = new Top(TopRecord.unsaved(
                topCode, topName),
                middles, nears);
        top.addMiddle(middleA).addMiddle(middleB).addMiddle(middleC);

        assertThat(top.getPlannedNears().map(Near::getCode))
                .containsExactly(nearCodeA);
    }

    @Test
    void shouldPreferPlannedNearButShowEsitmated() {
        final var nears = mock(Nears.class);
        final var estimatedNearRecord = spy(NearRecord.unsaved("FAR"));
        doReturn(estimatedNearRecord).when(estimatedNearRecord).save();
        final var estimatedNear = new Near(estimatedNearRecord);
        doReturn(Optional.of(estimatedNear)).when(nears)
                .byCode(estimatedNearRecord.code);
        final var plannedNearRecord = spy(NearRecord.unsaved(nearCode));
        doReturn(plannedNearRecord).when(plannedNearRecord).save();
        final var plannedNear = new Near(plannedNearRecord);
        doReturn(Optional.of(plannedNear)).when(nears)
                .byCode(plannedNearRecord.code);

        final var top = new Top(
                TopRecord.unsaved(topCode, topName)
                        .estimateNear(estimatedNearRecord)
                        .planNear(plannedNearRecord),
                mock(Middles.class), nears);

        assertThat(top.getEstimatedNear()).contains(estimatedNear);
        assertThat(top.getPlannedNear()).contains(plannedNear);
        assertThat(top.getPlannedNears()).containsOnly(plannedNear);
    }
}
