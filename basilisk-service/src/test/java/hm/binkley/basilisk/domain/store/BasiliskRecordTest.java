package hm.binkley.basilisk.domain.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasiliskRecordTest {
    @Mock
    private BasiliskStore store;

    @Test
    void shouldSave() {
        final BasiliskRecord unsaved = new BasiliskRecord(
                null, null, "FOO", Instant.ofEpochSecond(13L));
        unsaved.store = store;
        final BasiliskRecord saved = new BasiliskRecord(
                3L, EPOCH, unsaved.getWord(), unsaved.getAt());
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }
}
