package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
final class MyTestRecord
        extends StandardRecord<MyTestRecord, MyTestRepository,
        MyTestStore> {
    String code;
    int number;

    MyTestRecord(final Long id, final Instant receivedAt,
            final String code, final int number) {
        super(() -> new MyTestRecord(id, receivedAt, code, number),
                id, receivedAt);
    }
}
