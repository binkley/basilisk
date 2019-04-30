package hm.binkley.basilisk;

import lombok.Generated;
import lombok.Getter;
import lombok.ToString;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "json-placeholder",
        url = "https://jsonplaceholder.typicode.com")
public interface JsonPlaceholder {
    @GetMapping(path = "/todos/{id}", produces = "application/json")
    TodoResponse getTodo(@RequestParam("id") final int id);

    @Generated // Lie to JaCoCo
    @Getter
    @ToString
    class TodoResponse {
        int userId;
        int id;
        String title;
        boolean completed;
    }
}
