package ch.grignola.service.scanner.aave.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserReserf {

    @JsonProperty("reserve")
    public Reserve reserve;
    @JsonProperty("currentTotalDebt")
    public long currentTotalDebt;

}
