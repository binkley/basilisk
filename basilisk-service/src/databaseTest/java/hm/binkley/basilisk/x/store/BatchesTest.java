package hm.binkley.basilisk.x.store;

import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.kind.store.KindRepository;
import hm.binkley.basilisk.x.kind.store.KindStore;
import hm.binkley.basilisk.x.middle.Middle;
import hm.binkley.basilisk.x.middle.Middles;
import hm.binkley.basilisk.x.middle.store.MiddleRepository;
import hm.binkley.basilisk.x.middle.store.MiddleStore;
import hm.binkley.basilisk.x.near.Near;
import hm.binkley.basilisk.x.near.Nears;
import hm.binkley.basilisk.x.near.store.NearRepository;
import hm.binkley.basilisk.x.near.store.NearStore;
import hm.binkley.basilisk.x.top.Top;
import hm.binkley.basilisk.x.top.Tops;
import hm.binkley.basilisk.x.top.store.TopRepository;
import hm.binkley.basilisk.x.top.store.TopStore;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.springframework.transaction.annotation.Propagation.NESTED;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@Transactional
class BatchesTest {
    private final TopRepository topSpringData;
    private final MiddleRepository middleSpringData;

    private final Tops tops;
    private final Middles middles;

    @Autowired
    BatchesTest(final TopRepository topSpringData,
            final MiddleRepository middleSpringData,
            final KindRepository kindSpringData,
            final NearRepository nearSpringData) {
        this.topSpringData = spy(topSpringData);
        this.middleSpringData = spy(middleSpringData);

        final var nears = new Nears(new NearStore(nearSpringData));
        final var kinds = new Kinds(new KindStore(kindSpringData),
                nears);
        middles = new Middles(new MiddleStore(this.middleSpringData),
                kinds, nears);
        tops = new Tops(new TopStore(this.topSpringData),
                middles, nears);
    }

    @Test
    void shouldUpdateInTheFaceOfDanger() {
        final var middleCode = "MID";
        final var middleMid = 222;
        doThrow(UncategorizedSQLException.class).when(middleSpringData)
                .upsert(middleCode, null, middleMid);

        upsertMiddle(middleCode, middleMid);
        final var top = upsertTop("TOP", "TWIRL");

        verify(topSpringData).upsert(top.getCode(), top.getName(),
                top.getPlannedNear().map(Near::getCode).orElse(null));
    }

    @Transactional(propagation = NESTED)
    Middle upsertMiddle(final String code, final Integer mid) {
        final var unsaved = middles.unsaved(code, mid);
        try {
            return unsaved.save();
        } catch (final UncategorizedSQLException e) {
            return unsaved;
        }
    }

    @Transactional(propagation = NESTED)
    Top upsertTop(final String code, final String name) {
        return tops.unsaved(code, name).save();
    }
}
