package hm.binkley.basilisk.x.batch;

import hm.binkley.basilisk.x.middle.Middle;
import hm.binkley.basilisk.x.side.Side;
import hm.binkley.basilisk.x.top.Top;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.validation.constraints.NotNull;

import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Batches {
    private final PlatformTransactionManager transactionManager;

    public Batch unsaved(
            final @NotNull Side side,
            final Top top,
            final @NotNull Middle middle) {
        checkSide(side);
        checkMiddle(middle);
        return new Batch(transactionManager, side, top, middle);
    }

    private void checkSide(final Side side) {
        requireNonNull(side);
    }

    private void checkMiddle(final Middle middle) {
        requireNonNull(middle);
    }
}
