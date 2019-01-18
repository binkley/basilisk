package hm.binkley.basilisk.configuration;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Sets up the annotated test class as a {@link WebMvcTest @WebMvcTest} with
 * exclusively JSON payloads, both requests and responses.  Use this in place
 * of {@code WebMvcTest}.
 *
 * @see JsonMockMvcConfiguration
 * @see WebMvcTest @WebMvcTest
 */
@Documented
@Import(JsonMockMvcConfiguration.class)
@Retention(RUNTIME)
@Target(TYPE)
@WebMvcTest
public @interface JsonWebMvcTest {
    /** @see WebMvcTest#properties() */
    @AliasFor(annotation = WebMvcTest.class)
    String[] properties() default {};

    /** @see WebMvcTest#value() */
    @AliasFor(annotation = WebMvcTest.class)
    Class<?>[] value() default {};

    /** @see WebMvcTest#controllers() () */
    @AliasFor(annotation = WebMvcTest.class)
    Class<?>[] controllers() default {};

    /** @see WebMvcTest#useDefaultFilters() */
    @AliasFor(annotation = WebMvcTest.class)
    boolean useDefaultFilters() default true;

    /** @see WebMvcTest#includeFilters() */
    @AliasFor(annotation = WebMvcTest.class)
    ComponentScan.Filter[] includeFilters() default {};

    /** @see WebMvcTest#excludeFilters() */
    @AliasFor(annotation = WebMvcTest.class)
    ComponentScan.Filter[] excludeFilters() default {};

    /** @see WebMvcTest#excludeAutoConfiguration() */
    @AliasFor(annotation = WebMvcTest.class)
    Class<?>[] excludeAutoConfiguration() default {};
}
