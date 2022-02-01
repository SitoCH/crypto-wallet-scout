package ch.grignola.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

public class HistoricalAddressBalance {
    @JsonProperty("snapshots")
    public Map<OffsetDateTime, BigDecimal> snapshots;
}
