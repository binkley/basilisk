package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRecord;
import hm.binkley.basilisk.store.BasiliskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.ResponseEntity.created;

@RequestMapping("/basilisk")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class BasiliskController {
    private final BasiliskRepository repository;
    private final BasiliskService service;
    private final ServerProperties serverProperties;
    private final ErrorAttributes errorAttributes;

    private static HttpHeaders restHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);
        return headers;
    }

    @GetMapping
    public Page<BasiliskResponse> getAll(final Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::from);
    }

    @GetMapping("{id}")
    public BasiliskResponse findById(
            @PathVariable("id") final Long id) {
        return repository.findById(id)
                .map(this::from)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @GetMapping("find/{word}")
    public List<BasiliskResponse> getByWord(
            @Length(min = 3, max = 32) @PathVariable("word")
            final String word) {
        return repository.findByWord(word).stream()
                .map(this::from)
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<BasiliskResponse> postBasilisk(
            @RequestBody @Valid final BasiliskRequest request) {
        final BasiliskRecord saved = repository.save(request.toRecord());

        return created(URI.create("/basilisk/" + saved.getId()))
                .body(from(saved));
    }

    // This awkward method is sending the default Spring JSON for exceptions
    // from controllers as the response body.  There is trickiness, as the
    // exact properties in the JSON depend on YAML configuration.
    //
    // Also note: While HTTP status code 418 ("I am a teapot") is fully
    // present in the HTTP standards, it is the result of an April Fool's
    // joke gone wrong.  Used here solely as this is an example project.
    //
    // This whole thing highlights one of the shortcomings in Spring MVC
    // validation.
    @ExceptionHandler({
            ValidationException.class,
            MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> badContent(
            final WebRequest request, final Exception e) {
        return new ResponseEntity<>(
                errorBody(request, e),
                restHeaders(),
                UNPROCESSABLE_ENTITY);
    }

    private BasiliskResponse from(final BasiliskRecord record) {
        return BasiliskResponse.from(service, record);
    }

    private Map<String, Object> errorBody(final WebRequest request,
            final Exception e) {
        final boolean includeStackTrace = includeStackTrace(request);
        final Map<String, Object> body = errorAttributes
                .getErrorAttributes(request, includeStackTrace);
        body.put("error", e.getMessage());
        body.put("status", UNPROCESSABLE_ENTITY.value());
        return body;
    }

    private boolean includeStackTrace(final WebRequest request) {
        switch (serverProperties.getError().getIncludeStacktrace()) {
        case ALWAYS:
            return true;
        case ON_TRACE_PARAM:
            return isTraceEnabled(request);
        default:
            return false;
        }
    }

    private boolean isTraceEnabled(final WebRequest request) {
        return parseBoolean(request.getParameter("trace"));
    }
}
