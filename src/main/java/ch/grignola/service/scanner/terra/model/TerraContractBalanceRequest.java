package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerraContractBalanceRequest {

    @JsonProperty("balance")
    public Balance balance;

    public TerraContractBalanceRequest(String address) {
        this.balance = new Balance();
        this.balance.address = address;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Balance {

        @JsonProperty("address")
        public String address;

    }
}

