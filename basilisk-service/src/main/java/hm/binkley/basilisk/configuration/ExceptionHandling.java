package hm.binkley.basilisk.configuration;

import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.StatusType;
import org.zalando.problem.spring.web.advice.ProblemHandling;
import org.zalando.problem.spring.web.advice.security.SecurityAdviceTrait;

import java.util.NoSuchElementException;

import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ALWAYS;
import static org.zalando.problem.Status.NOT_FOUND;
import static org.zalando.problem.Status.UNPROCESSABLE_ENTITY;

@ControllerAdvice
@Generated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExceptionHandling
        implements ProblemHandling, SecurityAdviceTrait {
    private final ServerProperties server;

    @Override
    public StatusType defaultConstraintViolationStatus() {
        return UNPROCESSABLE_ENTITY;
    }

    @Override
    public boolean isCausalChainsEnabled() {
        return includeStackTrace(server);
    }

    static boolean includeStackTrace(final ServerProperties server) {
        return ALWAYS == server.getError().getIncludeStacktrace();
    }

    @ExceptionHandler
    public ResponseEntity<Problem> handleNoSuchElementException(
            final NoSuchElementException exception,
            final NativeWebRequest request) {
        return create(NOT_FOUND, exception, request);
    }
}
