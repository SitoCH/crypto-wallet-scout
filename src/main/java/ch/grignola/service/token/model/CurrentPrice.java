package ch.grignola.service.token.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("java:S116")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrentPrice {

    @JsonProperty("usd")
    public float usd;

}
