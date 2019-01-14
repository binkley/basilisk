package hm.binkley.basilisk.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Service
@Validated
public class BasiliskService {
    public String extra(@NotNull final String word) {
        return "Uncle Bob";
    }
}
