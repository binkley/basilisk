package hm.binkley.basilisk.x.near.rest;

import hm.binkley.basilisk.x.near.Nears;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

import static hm.binkley.basilisk.x.rest.NotFoundException.nearNotFound;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/nears")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Validated
@Transactional
public class NearsController {
    private final Nears nears;

    @GetMapping("/get")
    public List<NearResponse> getAll() {
        return nears.all()
                .map(NearResponse::of)
                .collect(toList());
    }

    @GetMapping("/get/{code}")
    public NearResponse get(@PathVariable("code") final String code) {
        return NearResponse.of(nears.byCode(code).orElseThrow());
    }

    /** @todo 200 vs 201 */
    @PostMapping("/post")
    public ResponseEntity<NearResponse> post(
            @RequestBody final @Valid NearRequest near) {
        final var saved = nears.unsaved(near.getCode()).save();

        return created(URI.create("/nears/get/" + saved.getCode()))
                .body(NearResponse.of(saved));
    }

    /** @todo Validation that code == near.code */
    @PutMapping("/put/{code}")
    public ResponseEntity<NearResponse> put(
            @RequestBody final @Valid NearRequest near,
            @PathVariable("code") final @NotNull String code) {
        final var exists = nears.byCode(code).isPresent();
        final var saved = nears.unsaved(near.getCode()).save();

        return exists
                ? ok(NearResponse.of(saved))
                : created(URI.create("/nears/get/" + saved.getCode()))
                        .body(NearResponse.of(saved));
    }

    @DeleteMapping("/delete/{code}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("code") final String code) {
        nears.byCode(code).orElseThrow(nearNotFound(code)).delete();
    }
}
