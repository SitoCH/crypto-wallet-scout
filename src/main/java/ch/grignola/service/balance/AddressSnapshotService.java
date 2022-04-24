package ch.grignola.service.balance;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public interface AddressSnapshotService {

    Map<OffsetDateTime, BigDecimal> getHistoricalAddressBalance(String address);

    Map<OffsetDateTime, BigDecimal> getHistoricalAddressesBalance(List<String> addresses);

    Map<LocalDate, BigDecimal> getHistoricalAddressBalanceWithFiatLots(String address);

    Map<LocalDate, BigDecimal> getHistoricalAddressesBalanceWithFiatLots(List<String> addresses);
}
