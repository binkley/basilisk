package hm.binkley.basilisk.x.kind.rest;

import hm.binkley.basilisk.x.kind.Kinds;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.created;

@RequestMapping("/kinds")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class KindsController {
    private final Kinds kinds;

    @GetMapping("/get")
    public List<KindResponse> getAll() {
        return kinds.all()
                .map(KindResponse::of)
                .collect(toList());
    }

    @GetMapping("/get/{code}")
    public KindResponse get(@PathVariable("code") final String code) {
        // TODO: Cleanest mapping NoSuchElementException -> 404 in context
        return KindResponse.of(kinds.byCode(code).get());
    }

    @PostMapping("/post")
    public ResponseEntity<KindResponse> post(
            @RequestBody final KindRequest kind) {
        final var saved = kinds.unsaved(kind.getCode(), kind.getCoolness())
                .save();

        return created(URI.create("/kinds/get/" + saved.getCode()))
                .body(KindResponse.of(saved));
    }
}
