package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class MyTestRecord
        extends StandardRecord<MyTestRecord, MyTestRepository,
        MyTestStore> {
    @Getter
    int number;

    public MyTestRecord(final Long id, final Instant receivedAt,
            final String code,
            final int number) {
        super(id, receivedAt, code);
        this.number = number;
    }

    public static MyTestRecord unsaved(final String code, final int number) {
        return new MyTestRecord(null, null, code, number);
    }
}
