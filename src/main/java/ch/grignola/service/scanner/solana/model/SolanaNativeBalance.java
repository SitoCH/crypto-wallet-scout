package ch.grignola.service.scanner.solana.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SolanaNativeBalance {
    @JsonProperty("lamports")
    public int lamports;
    @JsonProperty("ownerProgram")
    public String ownerProgram;
    @JsonProperty("type")
    public String type;
    @JsonProperty("rentEpoch")
    public int rentEpoch;
    @JsonProperty("account")
    public String account;
}
