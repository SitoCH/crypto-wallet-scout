package ch.grignola.service.scanner.cosmos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CosmosUnboundingBalance {

	@JsonProperty("height")
	public String height;
	@JsonProperty("result")
	public List<CosmosUnboundingResult> result = null;

}

