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
class StandardStoreTest {
    @Mock
    private final MyTestRepository repository;

    private MyTestStore store;

    @BeforeEach
    void setUp() {
        store = new MyTestStore(repository);
    }

    @Test
    void shouldSave() {
        final var record = MyTestRecord.unsaved("ABC", 2);

        store.save(record);

        verify(repository).save(record);
    }
}
