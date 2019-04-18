package hm.binkley.basilisk.x;

import hm.binkley.basilisk.x.kind.Kind;
import hm.binkley.basilisk.x.kind.store.KindRecord;
import hm.binkley.basilisk.x.near.Near;
import hm.binkley.basilisk.x.near.Nears;
import hm.binkley.basilisk.x.near.store.NearRecord;
import hm.binkley.basilisk.x.top.store.TopRecord;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

@NoArgsConstructor(access = PRIVATE)
public final class TestFixtures {
    public static NearRecord fixedNearRecord() {
        final var record = spy(new NearRecord());
        record.code = "NER";
        lenient().doReturn(record).when(record).save();
        return record;
    }

    public static Kind fixedKind(final @NotNull Nears nears) {
        return new Kind(fixedKindRecord(), nears);
    }

    public static KindRecord fixedKindRecord() {
        final var record = spy(new KindRecord());
        record.code = "KIN";
        record.coolness = new BigDecimal("2.3");
        lenient().doReturn(record).when(record).save();
        return record;
    }

    public static Near fixedNear() {
        return new Near(fixedNearRecord());
    }

    public static TopRecord fixedTopRecord() {
        final var record = spy(new TopRecord());
        record.code = "TOP";
        record.name = "TWIRL";
        lenient().doReturn(record).when(record).save();
        return record;
    }
}
