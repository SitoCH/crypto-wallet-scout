package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerraBalancesResponse {

    @JsonProperty("balances")
    public List<Balance> balances = null;
    @JsonProperty("pagination")
    public Pagination pagination;

}

