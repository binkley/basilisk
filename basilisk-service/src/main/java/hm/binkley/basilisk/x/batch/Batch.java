package hm.binkley.basilisk.x.batch;

import hm.binkley.basilisk.x.middle.Middle;
import hm.binkley.basilisk.x.side.Side;
import hm.binkley.basilisk.x.top.Top;
import lombok.Value;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.validation.constraints.NotNull;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_NESTED;

@Value
public class Batch {
    private static final String stale = "STALE";

    private final PlatformTransactionManager transactionManager;
    private final @NotNull Side side;
    private final Top top;
    private final @NotNull Middle middle;

    private static void ignoreIfStale(final UncategorizedSQLException e) {
        if (stale.equals(e.getSQLException().getSQLState()))
            return;
        throw e;
    }

    private void saveIgnoringStale(final Runnable save) {
        final var txn = transactionManager.getTransaction(
                new DefaultTransactionDefinition(PROPAGATION_NESTED));
        final var savepoint = txn.createSavepoint();
        try {
            save.run();
            txn.releaseSavepoint(savepoint);
        } catch (final UncategorizedSQLException maybeStale) {
            ignoreIfStale(maybeStale);
            txn.rollbackToSavepoint(savepoint);
        }
    }

    public void update() {
        saveIgnoringStale(side::save);
        if (null != top) saveIgnoringStale(top::save);
        saveIgnoringStale(middle::save);
    }
}
