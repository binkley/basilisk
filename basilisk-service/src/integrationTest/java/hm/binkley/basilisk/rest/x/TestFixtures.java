package hm.binkley.basilisk.rest.x;

import hm.binkley.basilisk.store.x.Kind;
import hm.binkley.basilisk.store.x.KindRecord;
import hm.binkley.basilisk.store.x.Near;
import hm.binkley.basilisk.store.x.NearRecord;
import hm.binkley.basilisk.store.x.Nears;
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
}
