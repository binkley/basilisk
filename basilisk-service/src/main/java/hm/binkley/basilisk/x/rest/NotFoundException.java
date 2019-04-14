package hm.binkley.basilisk.x.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;

import javax.annotation.Nullable;
import java.beans.ConstructorProperties;
import java.net.URI;
import java.util.function.Supplier;

import static org.zalando.problem.Status.NOT_FOUND;

@Getter
@ToString(callSuper = true)
public class NotFoundException
        extends AbstractThrowableProblem {
    public static String TYPE_VALUE
            = "https://binkley.hm/basilisk/not-found";
    public static URI TYPE = URI.create(TYPE_VALUE);

    private final String domain;
    private final String code;

    @ConstructorProperties({"type", "title", "status", "detail",
            "instance", "domain", "code"})
    @JsonCreator
    public NotFoundException(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final StatusType status,
            @Nullable final String detail,
            @Nullable final URI instance, final String domain,
            final String code) {
        super(type, title, status, detail, instance);
        this.domain = domain;
        this.code = code;
    }

    private NotFoundException(final String detail,
            final String domain, final String code) {
        this(TYPE, "Not Found", NOT_FOUND, detail,
                URI.create(String.format("%s/%s/%s", TYPE, domain, code)),
                domain, code);
    }

    public static Supplier<NotFoundException> nearNotFound(
            final String code) {
        return () -> new NotFoundException("Near not found: " + code,
                "near", code);
    }

    public static Supplier<NotFoundException> kindNotFound(
            final String code) {
        return () -> new NotFoundException("Kind not found: " + code,
                "kind", code);
    }
}
