package hm.binkley.basilisk.x.kind.rest;

import hm.binkley.basilisk.x.near.rest.ValidNear;
import lombok.Value;

import java.util.List;

@Value
public class PutNearsRequest {
    List<@ValidNear String> nearCodes;
}
