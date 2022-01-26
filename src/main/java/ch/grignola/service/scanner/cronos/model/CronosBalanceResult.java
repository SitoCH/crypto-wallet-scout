package ch.grignola.service.scanner.cronos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CronosBalanceResult {
    @JsonProperty("result")
    public Result result;
}
