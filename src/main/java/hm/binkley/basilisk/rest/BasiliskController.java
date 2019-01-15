package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRecord;
import hm.binkley.basilisk.store.BasiliskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;

@RequestMapping("/basilisk")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class BasiliskController {
    private final BasiliskRepository repository;
    private final BasiliskService service;

    @GetMapping
    public Page<BasiliskResponse> getAll(final Pageable pageable) {
        return repository.findAll(pageable)
                .map(this::from);
    }

    @GetMapping("{word}")
    public List<BasiliskResponse> getByWord(
            @PathVariable("word") @Length(min = 3, max = 32)
            final String word) {
        return repository.findByWord(word).stream()
                .map(this::from)
                .collect(toList());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = I_AM_A_TEAPOT)
    public void badContent() {
    }

    private BasiliskResponse from(final BasiliskRecord record) {
        return BasiliskResponse.from(service, record);
    }
}
