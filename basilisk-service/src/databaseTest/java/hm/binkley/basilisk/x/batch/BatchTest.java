package hm.binkley.basilisk.x.batch;

import hm.binkley.basilisk.store.BasiliskDatabaseTest;
import hm.binkley.basilisk.x.side.Sides;
import hm.binkley.basilisk.x.side.store.SideRepository;
import hm.binkley.basilisk.x.side.store.SideStore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

import static hm.binkley.basilisk.x.TestFixtures.fixedMiddle;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

@BasiliskDatabaseTest
class BatchTest {
    private final Batches batches;
    private final SideRepository sideSpringData;
    private final Sides sides;

    @Autowired
    BatchTest(final PlatformTransactionManager transactionManager,
            final SideRepository sideSpringData) {
        batches = new Batches(transactionManager);

        this.sideSpringData = spy(sideSpringData);
        sides = new Sides(new SideStore(this.sideSpringData));
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @Test
    void shouldPassThroughNonStaleExceptions() {
        final var sideCode = "SID";
        final var sequenceNumber = 2;
        final var side = sides.unsavedSequenced(sideCode, sequenceNumber);

        final var expectedException = new UncategorizedSQLException(
                "test", "SQL", new SQLException());

        doThrow(expectedException)
                .when(sideSpringData).upsert(sideCode, sequenceNumber);

        assertThatThrownBy(() ->
                batches.unsaved(
                        side,
                        null,
                        fixedMiddle(null, null))
                        .update())
                .isSameAs(expectedException);
    }
}
