package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pagination {

    @JsonProperty("next_key")
    public Object nextKey;
    @JsonProperty("total")
    public String total;

}
