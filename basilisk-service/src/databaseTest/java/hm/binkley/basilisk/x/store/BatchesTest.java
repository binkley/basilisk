package hm.binkley.basilisk.x.store;

import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.kind.store.KindRepository;
import hm.binkley.basilisk.x.kind.store.KindStore;
import hm.binkley.basilisk.x.middle.Middle;
import hm.binkley.basilisk.x.middle.Middles;
import hm.binkley.basilisk.x.middle.store.MiddleRepository;
import hm.binkley.basilisk.x.middle.store.MiddleStore;
import hm.binkley.basilisk.x.near.Nears;
import hm.binkley.basilisk.x.near.store.NearRepository;
import hm.binkley.basilisk.x.near.store.NearStore;
import hm.binkley.basilisk.x.side.Side;
import hm.binkley.basilisk.x.side.Sides;
import hm.binkley.basilisk.x.side.store.SideRepository;
import hm.binkley.basilisk.x.side.store.SideStore;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.springframework.transaction.annotation.Propagation.NESTED;

@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@Transactional
class BatchesTest {
    private final SideRepository sideSpringData;
    private final MiddleRepository middleSpringData;
    private final TopRepository topSpringData;

    private final Sides sides;
    private final Middles middles;
    private final Tops tops;

    @Autowired
    BatchesTest(final SideRepository sideSpringData,
            final MiddleRepository middleSpringData,
            final TopRepository topSpringData,
            final KindRepository kindSpringData,
            final NearRepository nearSpringData) {
        this.sideSpringData = spy(sideSpringData);
        this.middleSpringData = spy(middleSpringData);
        this.topSpringData = spy(topSpringData);

        final var nears = new Nears(new NearStore(nearSpringData));
        sides = new Sides(new SideStore(sideSpringData));
        final var kinds = new Kinds(new KindStore(kindSpringData),
                nears);
        middles = new Middles(new MiddleStore(this.middleSpringData),
                kinds, nears);
        tops = new Tops(new TopStore(this.topSpringData),
                middles, nears);
    }

    @Test
    void shouldUpdateInTheFaceOfDanger() {
        final var sideCode = "SID";
        doThrow(UncategorizedSQLException.class).when(sideSpringData)
                .upsert(sideCode, 0);
        final var middleCode = "MID";
        final var middleMid = 222;
        doThrow(UncategorizedSQLException.class).when(middleSpringData)
                .upsert(middleCode, null, middleMid, 0);

        final var side = upsertSide(sideCode);
        upsertMiddle(middleCode, side, middleMid);

        final var topCode = "TOP";
        final var topName = "TWIRL";
        final var top = upsertTop(topCode, side, topName);

        assertThat(top).isEqualTo(tops.unsaved(topCode, side, topName));
    }

    @Transactional(propagation = NESTED)
    Side upsertSide(final String code) {
        final var unsaved = sides.unsaved(code);
        try {
            return unsaved.save();
        } catch (final UncategorizedSQLException e) {
            return unsaved;
        }
    }

    @Transactional(propagation = NESTED)
    Middle upsertMiddle(
            final String code, final Side side, final Integer mid) {
        final var unsaved = middles.unsaved(code, side, mid);
        try {
            return unsaved.save();
        } catch (final UncategorizedSQLException e) {
            return unsaved;
        }
    }

    @Transactional(propagation = NESTED)
    Top upsertTop(final String code, final Side side, final String name) {
        return tops.unsaved(code, side, name).save();
    }
}
