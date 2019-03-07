package hm.binkley.basilisk.basilisk.rest;

import hm.binkley.basilisk.basilisk.domain.Basilisk;
import hm.binkley.basilisk.basilisk.domain.Basilisks;
import hm.binkley.basilisk.basilisk.service.BasiliskService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.created;

@RequestMapping("/basilisk")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class BasiliskController {
    private final Basilisks basilisks;
    private final BasiliskService service;

    @GetMapping
    public List<BasiliskResponse> getAll() {
        return basilisks.all()
                .map(toResponse())
                .collect(toList());
    }

    @GetMapping("{id}")
    public BasiliskResponse findById(
            @PathVariable("id") final Basilisk basilisk) {
        return toResponse().apply(basilisk);
    }

    @GetMapping("find/{word}")
    public List<BasiliskResponse> findByWord(
            @PathVariable("word") final @Length(min = 3, max = 32)
                    String word) {
        return basilisks.byWord(word)
                .map(toResponse())
                .collect(toList());
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public ResponseEntity<BasiliskResponse> postBasilisk(
            @RequestBody final @Valid BasiliskRequest request) {
        final var basilisk = basilisks.create(request);
        final var response = toResponse().apply(basilisk);

        return created(URI.create("/basilisk/" + response.getId()))
                .body(response);
    }

    private Function<Basilisk, BasiliskResponse> toResponse() {
        return it -> it.as(
                BasiliskResponse.using(service), CockatriceResponse.using());
    }

    /** @todo Think more deeply about global controller advice */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(NOT_FOUND)
    @SuppressWarnings("PMD")
    private void handleNotFound() {
    }
}