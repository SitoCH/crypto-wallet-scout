package ch.grignola.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class HistoricalAddressBalanceWithLots {
    @JsonProperty("snapshots")
    public Map<LocalDate, BigDecimal> snapshots;
}
