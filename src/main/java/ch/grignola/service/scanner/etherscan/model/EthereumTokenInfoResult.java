package ch.grignola.service.scanner.etherscan.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EthereumTokenInfoResult {

    @JsonProperty("status")
    public String status;
    @JsonProperty("message")
    public String message;
    @JsonProperty("result")
    public List<EthereumTokenInfo> result = null;

}
