package ch.grignola.service.scanner.terra.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerraRewards {

    @JsonProperty("total")
    public String total;
    @JsonProperty("denoms")
    public List<TerraDenom> denoms = null;

}
