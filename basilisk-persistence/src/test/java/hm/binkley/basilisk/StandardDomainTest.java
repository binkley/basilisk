package hm.binkley.basilisk;

import hm.binkley.basilisk.store.MyTestRecord;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class StandardDomainTest {
    @Mock
    private final MyTestRecord record;

    private MyTestDomain domain;

    @BeforeEach
    void setUp() {
        domain = new MyTestDomain(record);
    }

    @Test
    void shouldHaveId() {
        final var id = 1L;
        when(record.getId())
                .thenReturn(id);

        assertThat(domain.getId()).isEqualTo(id);
    }

    @Test
    void shouldHaveCode() {
        final var code = "ABC";
        when(record.getCode())
                .thenReturn(code);

        assertThat(domain.getCode()).isEqualTo(code);
    }

    @Test
    void shouldSave() {
        final var saved = domain.save();

        assertThat(saved).isSameAs(domain);

        verify(record).save();
        verifyNoMoreInteractions(record);
    }

    @Test
    void shouldDelete() {
        final var deleted = domain.delete();

        assertThat(deleted).isSameAs(domain);

        verify(record).delete();
        verifyNoMoreInteractions(record);
    }
}
