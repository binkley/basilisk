package hm.binkley.basilisk.store.x;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class DomainsTest {
    @Test
    void shouldDelegateToRecord() {
        final var topCode = "TOP";
        final var top = new Top(TopRecord.unsaved(
                topCode, "TWIRL"),
                mock(Middles.class));

        assertThat(top.getCode()).isEqualTo(topCode);

        final var kindCode = "KIN";
        final var kind = new Kind(KindRecord.unsaved(
                kindCode, new BigDecimal("2.3")));

        assertThat(kind.getCode()).isEqualTo(kindCode);

        final var middleCode = "MID";
        final var middle = new Middle(MiddleRecord.unsaved(
                middleCode, 222), mock(Kinds.class));

        assertThat(middle.getCode()).isEqualTo(middleCode);
    }
}
