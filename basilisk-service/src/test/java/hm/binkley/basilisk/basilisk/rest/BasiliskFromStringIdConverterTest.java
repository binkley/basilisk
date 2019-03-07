package hm.binkley.basilisk.basilisk.rest;

import hm.binkley.basilisk.basilisk.domain.Basilisk;
import hm.binkley.basilisk.basilisk.domain.Basilisks;
import hm.binkley.basilisk.basilisk.domain.store.BasiliskRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasiliskFromStringIdConverterTest {
    @Mock
    private Basilisks basilisks;

    private BasiliskFromStringIdConverter converter;

    @BeforeEach
    void setUp() {
        converter = new BasiliskFromStringIdConverter(basilisks);
    }

    @Test
    void shouldConvert() {
        final var record = new BasiliskRecord(
                3L, EPOCH, "FOO", Instant.ofEpochSecond(1L));
        final var basilisk = new Basilisk(record);
        when(basilisks.byId(record.getId()))
                .thenReturn(Optional.of(basilisk));

        assertThat(converter.convert(String.valueOf(record.getId())))
                .isEqualTo(basilisk);

        verifyNoMoreInteractions(basilisks);
    }

    @Test
    void shouldNotConvert() {
        assertThatThrownBy(() -> converter.convert(String.valueOf(3L)))
                .isInstanceOf(NoSuchElementException.class);
    }
}
