package ch.grignola.service.scanner.etherscan.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EthereumTokenInfo {

    @JsonProperty("blueCheckmark")
    public boolean blueCheckmark;

}
