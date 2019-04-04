package hm.binkley.basilisk.store.x;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class DomainsTest {
    @Test
    void shouldDelegateToRecord() {
        final var sideCode = "SID";
        final var sideRecord = spy(SideRecord.unsaved(
                sideCode, Instant.ofEpochMilli(1_000_000)));
        doReturn(sideRecord).when(sideRecord).save();
        final var side = new Side(sideRecord);
        final var sides = mock(Sides.class);
        when(sides.byCode(sideCode)).thenReturn(Optional.of(side));

        assertThat(side.getCode()).isEqualTo(sideCode);

        final var topCode = "TOP";
        final var top = new Top(TopRecord.unsaved(
                topCode, "TWIRL", sideRecord),
                mock(Middles.class), sides);

        assertThat(top.getCode()).isEqualTo(topCode);
        assertThat(top.getTime()).isEqualTo(sideRecord.time);

        final var kindCode = "KIN";
        final var kindRecord = spy(KindRecord.unsaved(
                kindCode, new BigDecimal("2.3")));
        doReturn(kindRecord).when(kindRecord).save();
        final var kind = new Kind(kindRecord);
        final var kinds = mock(Kinds.class);
        when(kinds.byCode(kindCode)).thenReturn(Optional.of(kind));

        assertThat(kind.getCode()).isEqualTo(kindCode);

        final var middleCode = "MID";
        final var middle = new Middle(MiddleRecord.unsaved(
                middleCode, 222), kinds, sides)
                .defineKind(kind)
                .defineSide(side);

        assertThat(middle.getCode()).isEqualTo(middleCode);
        assertThat(middle.getCoolness()).isEqualTo(kindRecord.coolness);
        assertThat(middle.getTime()).isEqualTo(sideRecord.time);
    }
}
