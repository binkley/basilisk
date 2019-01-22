package hm.binkley.basilisk.configuration;

import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ALWAYS;
import static org.zalando.problem.Status.UNPROCESSABLE_ENTITY;

@ControllerAdvice
@Generated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BasiliskExceptionHandling
        implements ProblemHandling {
    private final ServerProperties server;

    private static boolean includeStackTrace(final ServerProperties server) {
        return ALWAYS == server.getError().getIncludeStacktrace();
    }

    @Override
    public StatusType defaultConstraintViolationStatus() {
        return UNPROCESSABLE_ENTITY;
    }

    @Override
    public boolean isCausalChainsEnabled() {
        return includeStackTrace(server);
    }
}
