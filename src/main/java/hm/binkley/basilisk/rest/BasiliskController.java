package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.service.BasiliskService;
import hm.binkley.basilisk.store.BasiliskRecord;
import hm.binkley.basilisk.store.BasiliskRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.created;

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

    private BasiliskResponse from(final BasiliskRecord record) {
        return BasiliskResponse.from(service, record);
    }
}
