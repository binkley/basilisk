package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.I_AM_A_TEAPOT;

@RequestMapping("/basilisk")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@Validated
public class BasiliskController {
    private final BasiliskRepository repository;
    private final BasiliskService service;

    @GetMapping("{word}")
    public List<BasiliskResponse> getByWord(
            @PathVariable("word") @Size(min = 3, max = 32)
            final String word) {
        return repository.findByWord(word).stream()
                .map(record -> BasiliskResponse.builder()
                        .word(record.getWord())
                        .when(record.getWhen().atOffset(UTC))
                        .extra(service.extra(record.getWord()))
                        .build())
                .collect(toList());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = I_AM_A_TEAPOT)
    public void badContent() {
    }
}
