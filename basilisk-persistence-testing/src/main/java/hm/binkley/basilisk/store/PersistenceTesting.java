package hm.binkley.basilisk.store;

import lombok.NoArgsConstructor;
import org.mockito.stubbing.Answer;

import java.time.Instant;

@NoArgsConstructor
public final class PersistenceTesting {
    public static <T extends StandardRecord<T, R, S>,
            R extends StandardRepository<T, R, S>,
            S extends StandardStore<T, R, S>>
    Answer<T> simulateRepositorySave(
            final Long newId, final Instant newReceivedAt) {
        return invocation -> {
            final T record = invocation.getArgument(0);
            record.id = newId;
            record.receivedAt = newReceivedAt;
            return record;
        };
    }

    public static <T extends StandardRecord<T, R, S>,
            R extends StandardRepository<T, R, S>,
            S extends StandardStore<T, R, S>>
    Answer<T> simulateRepositoryDelete() {
        return invocation -> {
            final T record = invocation.getArgument(0);
            record.id = null;
            record.receivedAt = null;
            return record;
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends StandardRecord<T, R, S>,
            R extends StandardRepository<T, R, S>,
            S extends StandardStore<T, R, S>>
    Answer<T> simulateRecordSave(
            final Long newId, final Instant newReceivedAt) {
        return invocation -> {
            final T record = (T) invocation.getMock();
            record.id = newId;
            record.receivedAt = newReceivedAt;
            return record;
        };
    }

    @SuppressWarnings("unchecked")
    public static <T extends StandardRecord<T, R, S>,
            R extends StandardRepository<T, R, S>,
            S extends StandardStore<T, R, S>>
    Answer<T> simulateRecordDelete() {
        return invocation -> {
            final T record = (T) invocation.getMock();
            record.id = null;
            record.receivedAt = null;
            return record;
        };
    }
}
