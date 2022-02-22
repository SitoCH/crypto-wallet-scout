package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerraContractBalanceRequest {

    @JsonProperty("balance")
    public Balance balance;

    public TerraContractBalanceRequest(String address) {
        this.balance = new Balance();
        this.balance.address = address;
    }

    @RegisterForReflection
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class Balance {

        @JsonProperty("address")
        public String address;

    }
}

