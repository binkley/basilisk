package hm.binkley.basilisk;

import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassRelativeResourceLoader;

import java.io.IOException;
import java.lang.StackWalker.StackFrame;
import java.util.Locale;
import java.util.regex.Pattern;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.util.StreamUtils.copyToString;

@NoArgsConstructor
public final class TestJson {
    private static final StackWalker stackWalker = StackWalker.getInstance(
            RETAIN_CLASS_REFERENCE);
    private static final Pattern capitals = Pattern.compile("([A-Z])");

    public static String readTestJson(final String tag)
            throws IOException {
        final StackFrame testMethodFrame = stackWalker.walk(frames -> frames
                .skip(1) // Ignore ourselves
                .filter(it -> it.getMethodName().startsWith("should"))
                .findFirst()
                .orElseThrow());
        final var testClass = testMethodFrame.getDeclaringClass();

        return copyToString(new ClassRelativeResourceLoader(testClass)
                .getResource(jsonFileName(testClass, testMethodFrame, tag))
                .getInputStream(), UTF_8);
    }

    private static String jsonFileName(final Class<?> testClass,
            final StackFrame testFrame, final String tag) {
        return format("%s-%s-%s.json",
                toKebabCase(testClass.getSimpleName(), "-"),
                toKebabCase(testFrame.getMethodName(), "should-"),
                tag);
    }

    private static String toKebabCase(
            final String text, final String removePrefix) {
        return capitals.matcher(text)
                .replaceAll(matched ->
                        "-" + matched.group(1).toLowerCase(Locale.ROOT))
                .substring(removePrefix.length());
    }
}
