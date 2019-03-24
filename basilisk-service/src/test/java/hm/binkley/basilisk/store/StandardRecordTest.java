package hm.binkley.basilisk.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class StandardRecordTest {
    @Mock
    private final MyTestStore store;

    private MyTestRecord record;

    @BeforeEach
    void setUp() {
        record = MyTestRecord.unsaved("ABC", 123);
        record.store = store;
    }

    @Test
    void shouldSave() {
        record.save();

        verify(store).save(record);
    }

    @Test
    void shouldDelete() {
        record.delete();

        verify(store).delete(record);
    }
}
