package ch.grignola.service.scanner.cro.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CroBalanceResult {
    @JsonProperty("result")
    public Result result;
}
