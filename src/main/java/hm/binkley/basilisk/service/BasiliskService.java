package hm.binkley.basilisk.service;

import hm.binkley.basilisk.configuration.BasiliskProperties;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@EnableConfigurationProperties(BasiliskProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Validated
public class BasiliskService {
    private final BasiliskProperties basilisk;

    public String extra(
            final @Length(min = 3, max = 32) @NotNull String word) {
        return basilisk.getExtraWord() + " is " + word;
    }
}
