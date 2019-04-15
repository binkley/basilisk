package hm.binkley.basilisk.x.near.rest;

import hm.binkley.basilisk.x.near.Nears;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ValidNearValidator
        implements ConstraintValidator<ValidNear, String> {
    private final Nears nears;

    @Override
    public boolean isValid(final String code,
            final ConstraintValidatorContext context) {
        return nears.exists(code);
    }
}
