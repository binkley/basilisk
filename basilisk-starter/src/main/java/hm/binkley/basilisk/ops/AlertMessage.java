package hm.binkley.basilisk.ops;

import lombok.experimental.UtilityClass;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static java.lang.Thread.currentThread;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
public @interface AlertMessage {
    String message();

    Severity severity();

    enum Severity {
        LOW, MEDIUM, HIGH
    }

    @UtilityClass
    class MessageFinder {
        public static AlertMessage findAlertMessage(
                final Throwable throwable) {
            final var throwables = new ArrayList<Throwable>();
            for (Throwable x = throwable; null != x; x = x.getCause())
                throwables.add(x);

            // Search bottom up
            final var lit = throwables.listIterator(throwables.size());
            while (lit.hasPrevious()) {
                final var message = findAlertMessage(
                        lit.previous().getStackTrace());
                if (null != message)
                    return message;
            }

            return null;
        }

        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        private static AlertMessage findAlertMessage(
                final StackTraceElement... stackTrace) {
            for (final var frame : stackTrace) {
                final var message = findAlertMessage(frame);
                if (null != message)
                    return message;
            }

            return null;
        }

        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        private static AlertMessage findAlertMessage(
                final StackTraceElement frame) {
            try {
                final var methods = currentThread()
                        .getContextClassLoader()
                        .loadClass(frame.getClassName())
                        .getDeclaredMethods();

                for (final var method : methods) {
                    final var message = findAlertMessage(
                            frame.getMethodName(), method);
                    if (null != message)
                        return message;
                }

                return null;
            } catch (final ClassNotFoundException e) {
                // Ignore unloadable frames, eg, JUnit, Gradle, etc
                return null;
            }
        }

        private static AlertMessage findAlertMessage(final String methodName,
                final Method method) {
            if (!methodName.equalsIgnoreCase(method.getName()))
                return null;

            return findAlertMessage(method);
        }

        private static AlertMessage findAlertMessage(final Method method) {
            return findAnnotation(method, AlertMessage.class);
        }
    }
}
