package ch.grignola.service.scanner.polkadot.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Data {

    @JsonProperty("balances")
    public Balances balances;
    @JsonProperty("info")
    public Info info;
    @JsonProperty("address")
    public String address;
    @JsonProperty("displayName")
    public String displayName;

}
