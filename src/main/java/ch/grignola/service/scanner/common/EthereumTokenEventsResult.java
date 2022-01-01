package ch.grignola.service.scanner.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public
class EthereumTokenEventsResult {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("result")
    private List<EthereumTokenEventResult> result = null;

    public List<EthereumTokenEventResult> getResult() {
        return result;
    }

    public void setResult(List<EthereumTokenEventResult> result) {
        this.result = result;
    }
}
