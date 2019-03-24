package hm.binkley.basilisk.store;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.Invocation;

import java.time.Instant;

import static hm.binkley.basilisk.store.PersistenceTesting.simulateRecordDelete;
import static hm.binkley.basilisk.store.PersistenceTesting.simulateRecordSave;
import static hm.binkley.basilisk.store.PersistenceTesting.simulateRepositoryDelete;
import static hm.binkley.basilisk.store.PersistenceTesting.simulateRepositorySave;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PersistenceTestingTest {
    private static final Long ID = 1L;
    private static final Instant RECEIVED_AT = EPOCH;
    private static final String CODE = "ABC";
    private static final int NUMBER = 222;

    @Test
    void shouldSimulateRepositorySaving()
            throws Throwable {
        final var record = MyTestRecord.unsaved(CODE, NUMBER);
        final var invocation = mock(Invocation.class);
        when(invocation.getArgument(0))
                .thenReturn(record);

        final var answered = simulateRepositorySave(ID, RECEIVED_AT)
                .answer(invocation);

        assertThat(answered.getId()).isEqualTo(ID);
        assertThat(answered.getReceivedAt()).isEqualTo(RECEIVED_AT);
    }

    @Test
    void shouldSimulateRepositoryDeletion()
            throws Throwable {
        final var record = MyTestRecord.unsaved(CODE, NUMBER);
        record.id = ID;
        record.receivedAt = RECEIVED_AT;
        final var invocation = mock(Invocation.class);
        when(invocation.getArgument(0))
                .thenReturn(record);

        final var answered = simulateRepositoryDelete()
                .answer(invocation);

        assertThat(answered.getId()).isNull();
        assertThat(answered.getReceivedAt()).isNull();
    }

    @Test
    void shouldSimulateRecordSaving()
            throws Throwable {
        final var record = MyTestRecord.unsaved(CODE, NUMBER);
        final var invocation = mock(Invocation.class);
        when(invocation.getMock())
                .thenReturn(record);

        final var answered = simulateRecordSave(ID, RECEIVED_AT)
                .answer(invocation);

        assertThat(answered.getId()).isEqualTo(ID);
        assertThat(answered.getReceivedAt()).isEqualTo(RECEIVED_AT);
    }

    @Test
    void shouldSimulateRecordDeletion()
            throws Throwable {
        final var record = MyTestRecord.unsaved(CODE, NUMBER);
        record.id = ID;
        record.receivedAt = RECEIVED_AT;
        final var invocation = mock(Invocation.class);
        when(invocation.getMock())
                .thenReturn(record);

        final var answered = simulateRecordDelete()
                .answer(invocation);

        assertThat(answered.getId()).isNull();
        assertThat(answered.getReceivedAt()).isNull();
    }
}
