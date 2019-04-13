package hm.binkley.basilisk.x.near.rest;

import lombok.Value;

import javax.validation.constraints.NotEmpty;

@Value
public final class NearRequest {
    @NotEmpty
    String code;
}
