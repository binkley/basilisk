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

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ALWAYS;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@RequestMapping("/basilisk")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class BasiliskController {
    private final BasiliskRepository repository;
    private final BasiliskService service;
    private final ServerProperties serverProperties;
    private final ErrorAttributes errorAttributes;

    @GetMapping
    public Page<BasiliskResponse> getAll(final Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::from);
    }

    @GetMapping("{word}")
    public List<BasiliskResponse> getByWord(
            @Length(min = 3, max = 32) @PathVariable("word")
            final String word) {
        return repository.findByWord(word).stream()
                .map(this::from)
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public Long postBasilisk(
            @RequestBody @Valid final BasiliskRequest request) {
        return repository.save(request.toRecord()).getId();
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
            ConstraintViolationException.class,
            MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> badContent(
            final WebRequest request, final Exception e) {
        final boolean includeStackTrace =
                ALWAYS == serverProperties.getError().getIncludeStacktrace();
        final Map<String, Object> body = errorAttributes
                .getErrorAttributes(request, includeStackTrace);
        body.put("error", e.getMessage());
        body.put("status", I_AM_A_TEAPOT.value());

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON_UTF8);

        return new ResponseEntity<>(body, headers, I_AM_A_TEAPOT);
    }

    private BasiliskResponse from(final BasiliskRecord record) {
        return BasiliskResponse.from(service, record);
    }
}
