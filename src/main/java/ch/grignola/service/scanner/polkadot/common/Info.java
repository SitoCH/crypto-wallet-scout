package ch.grignola.service.scanner.polkadot.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Info {

    @JsonProperty("displayName")
    public String displayName;
    @JsonProperty("identity")
    public Identity identity;

}
