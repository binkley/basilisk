package hm.binkley.basilisk.x.near.rest;

import hm.binkley.basilisk.x.near.Nears;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.created;

@RequestMapping("/nears")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
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
            @RequestBody final NearRequest near) {
        final var saved = nears.unsaved(near.getCode()).save();

        return created(URI.create("/nears/get/" + saved.getCode()))
                .body(NearResponse.of(saved));
    }

    @DeleteMapping("/delete/{code}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable("code") final String code) {
        nears.byCode(code).orElseThrow().delete();
    }
}
