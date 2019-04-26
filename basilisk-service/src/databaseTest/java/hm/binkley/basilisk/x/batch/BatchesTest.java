package hm.binkley.basilisk.x.batch;

import hm.binkley.basilisk.store.BasiliskDatabaseTest;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.Assertions.assertThat;

@BasiliskDatabaseTest
class BatchesTest {
    private static final int sequenceNumber = 2;
    private static final int staleSequenceNumber = sequenceNumber - 1;
    private static final int freshSequenceNumber = sequenceNumber + 1;

    private final Batches batches;
    private final Sides sides;
    private final Middles middles;
    private final Tops tops;
    private Side side;
    private Top top;
    private Middle middle;

    @Autowired
    BatchesTest(final PlatformTransactionManager transactionManager,
            final SideRepository sideSpringData,
            final MiddleRepository middleSpringData,
            final TopRepository topSpringData,
            final KindRepository kindSpringData,
            final NearRepository nearSpringData) {
        batches = new Batches(transactionManager);

        final var nears = new Nears(new NearStore(nearSpringData));
        sides = new Sides(new SideStore(sideSpringData));
        final var kinds = new Kinds(new KindStore(kindSpringData), nears);
        middles = new Middles(new MiddleStore(middleSpringData), kinds,
                nears);
        tops = new Tops(new TopStore(topSpringData), middles, nears);
    }

    @BeforeEach
    void setUp() {
        side = sides.unsavedSequenced("SID", sequenceNumber)
                .save();
        top = tops.unsavedSequenced("TOP", side, "TWIRL", sequenceNumber)
                .save();
        middle = middles.unsavedSequenced("MID", side, 222, sequenceNumber);
    }

    @Test
    void shouldSaveMiddleWithStaleSideAndTop() {
        final var updatedMid = middle.getMid();

        batches.unsaved(
                sides.unsavedSequenced(side.getCode(),
                        staleSequenceNumber),
                tops.unsavedSequenced(top.getCode(), side, top.getName(),
                        staleSequenceNumber),
                middles.unsavedSequenced(middle.getCode(), side, updatedMid,
                        freshSequenceNumber))
                .update();

        assertThat(middles.byCode(middle.getCode()).orElseThrow().getMid())
                .isEqualTo(updatedMid);
    }

    @Test
    void shouldSaveMiddleWithStaleSideAndNoTop() {
        final var updatedMid = middle.getMid();

        batches.unsaved(
                sides.unsavedSequenced(side.getCode(),
                        staleSequenceNumber),
                null,
                middles.unsavedSequenced(middle.getCode(), side, updatedMid,
                        freshSequenceNumber))
                .update();

        assertThat(middles.byCode(middle.getCode()).orElseThrow().getMid())
                .isEqualTo(updatedMid);
    }

    @Test
    void shouldIgnoreBatchWithStaleStaleSideTopAndMiddle() {
        final var updatedMid = middle.getMid();

        batches.unsaved(
                sides.unsavedSequenced(side.getCode(),
                        staleSequenceNumber),
                tops.unsavedSequenced(top.getCode(), side, top.getName(),
                        staleSequenceNumber),
                middles.unsavedSequenced(middle.getCode(), side, updatedMid,
                        staleSequenceNumber))
                .update();

        assertThat(middles.byCode(middle.getCode()).orElseThrow().getMid())
                .isEqualTo(middle.getMid());
    }
}
