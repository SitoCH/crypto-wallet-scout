package ch.grignola.service.scanner.cosmos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CosmosUnboundingBalanceEntry {

    @JsonProperty("creation_height")
    public String creationHeight;
    @JsonProperty("completion_time")
    public String completionTime;
    @JsonProperty("initial_balance")
    public String initialBalance;
    @JsonProperty("balance")
    public long balance;

}
