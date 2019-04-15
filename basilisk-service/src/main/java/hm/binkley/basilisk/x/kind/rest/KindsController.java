package hm.binkley.basilisk.x.kind.rest;

import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.near.Nears;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static hm.binkley.basilisk.x.rest.NotFoundException.kindNotFound;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RequestMapping("/kinds")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Transactional
@Validated
public class KindsController {
    private final Kinds kinds;
    private final Nears nears;

    @GetMapping("/get")
    public List<KindResponse> getAll() {
        return kinds.all()
                .map(KindResponse::of)
                .collect(toList());
    }

    @GetMapping("/get/{code}")
    public KindResponse get(@PathVariable("code") final String code) {
        return KindResponse.of(kinds.byCode(code)
                .orElseThrow(kindNotFound(code)));
    }

    @PostMapping("/post")
    public ResponseEntity<KindResponse> post(
            @RequestBody final @Valid KindRequest kind) {
        return makeOne(kind);
    }

    /** @todo Validation that code == kind.code */
    @PutMapping("/put/{code}")
    public ResponseEntity<KindResponse> put(
            @PathVariable("code") final @NotNull String code,
            @RequestBody final @Valid KindRequest kind) {
        return makeOne(kind);
    }

    /** @todo 422 for near already in kind? */
    @PostMapping("/post/{code}/nears/add")
    public KindResponse postNearsAdd(
            @PathVariable("code") final @NotNull String code,
            @RequestBody final @Valid PutNearsRequest nears) {
        final var kind = kinds.byCode(code).orElseThrow(kindNotFound(code));
        nears.getNearCodes().stream()
                .map(this.nears::byCode)
                .map(Optional::orElseThrow) // Already validated
                .forEach(kind::addNear);

        return KindResponse.of(kind.save());
    }

    /** @todo 422 for near not already in kind? */
    @PostMapping("/post/{code}/nears/remove")
    public KindResponse postNearsRemove(
            @PathVariable("code") final @NotNull String code,
            @RequestBody final @Valid PutNearsRequest nears) {
        final var kind = kinds.byCode(code).orElseThrow(kindNotFound(code));
        nears.getNearCodes().stream()
                .map(this.nears::byCode)
                .map(Optional::orElseThrow) // Already validated
                .forEach(kind::removeNear);

        return KindResponse.of(kind.save());
    }

    @DeleteMapping("/delete/{code}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("code") final String code) {
        kinds.byCode(code).orElseThrow(kindNotFound(code)).delete();
    }

    private ResponseEntity<KindResponse> makeOne(final KindRequest kind) {
        final var exists = kinds.byCode(kind.getCode()).isPresent();
        final var saved = kinds.unsaved(kind.getCode(), kind.getCoolness())
                .save();

        final var headers = new HttpHeaders();
        headers.setLocation(URI.create("/kinds/get/" + saved.getCode()));

        return new ResponseEntity<>(KindResponse.of(saved), headers,
                exists ? OK : CREATED);
    }
}
