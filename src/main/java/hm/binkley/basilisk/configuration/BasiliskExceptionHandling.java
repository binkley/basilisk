package hm.binkley.basilisk.configuration;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

@ControllerAdvice
public class BasiliskExceptionHandling
        implements ProblemHandling {
}
