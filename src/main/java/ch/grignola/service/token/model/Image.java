package ch.grignola.service.token.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image {

    @JsonProperty("thumb")
    public String thumb;
    @JsonProperty("small")
    public String small;
    @JsonProperty("large")
    public String large;

}
