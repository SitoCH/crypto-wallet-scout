package ch.grignola.service.scanner.polkadot.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Identity {

    @JsonProperty("judgements")
    public List<Object> judgements = null;

}
