package hm.binkley.basilisk.configuration;

import hm.binkley.basilisk.ops.Alerter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ALWAYS;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.NEVER;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class ExceptionHandlingTest {
    @Mock
    private final ServerProperties server;
    @Mock
    private final ErrorProperties error;
    @Mock
    private final Logger logger;
    @Mock
    private final Alerter alerter;

    private ExceptionHandling handling;

    @BeforeEach
    void setUp() {
        given(server.getError()).willReturn(error);

        handling = new ExceptionHandling(server, logger, alerter);
    }

    @Test
    void shouldLogCausalChainsOnlyIfConfiguredToDoSo() {
        given(error.getIncludeStacktrace())
                .willReturn(ALWAYS);

        assertThat(handling.isCausalChainsEnabled()).isTrue();
    }

    // TODO: What if caller asks for stack traces in the query?

    @Test
    void shouldNotLogCausalChainsOnlyIfConfiguredToDoSo() {
        given(error.getIncludeStacktrace())
                .willReturn(NEVER);

        assertThat(handling.isCausalChainsEnabled()).isFalse();
    }
}
