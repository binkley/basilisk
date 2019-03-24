package hm.binkley.basilisk;

import hm.binkley.basilisk.store.MyTestRecord;
import hm.binkley.basilisk.store.MyTestStore;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class StandardFactoryTest {
    private static final Long ID = 1L;
    private static final Instant RECEIVED_AT = EPOCH;
    private static final String CODE = "ABC";
    private static final int NUMBER = 222;

    @Mock
    private final MyTestStore store;

    private MyTestFactory factory;

    @BeforeEach
    void setUp() {
        factory = new MyTestFactory(store);
    }

    @Test
    void shouldFindById() {
        final var saved = new MyTestRecord(ID, RECEIVED_AT, CODE, NUMBER);
        when(store.byId(ID))
                .thenReturn(Optional.of(saved));

        final var found = factory.byId(ID).orElseThrow();

        assertThat(found).isEqualTo(new MyTestDomain(saved));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByCode() {
        final var saved = new MyTestRecord(ID, RECEIVED_AT, CODE, NUMBER);
        when(store.byCode(CODE))
                .thenReturn(Optional.of(saved));

        final var found = factory.byCode(CODE).orElseThrow();

        assertThat(found).isEqualTo(new MyTestDomain(saved));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var saved = new MyTestRecord(ID, RECEIVED_AT, CODE, NUMBER);
        when(store.all())
                .thenReturn(Stream.of(saved));

        final var found = factory.all();

        assertThat(found).containsExactly(new MyTestDomain(saved));

        verifyNoMoreInteractions(store);
    }
}
