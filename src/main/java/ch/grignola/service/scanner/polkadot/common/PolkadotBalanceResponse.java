package ch.grignola.service.scanner.polkadot.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PolkadotBalanceResponse {

    @JsonProperty("data")
    public Data data;
    @JsonProperty("error")
    public Object error;

}