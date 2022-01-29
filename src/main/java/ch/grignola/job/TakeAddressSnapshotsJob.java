package ch.grignola.job;

import ch.grignola.model.AddressSnapshot;
import ch.grignola.model.UserCollectionAddress;
import ch.grignola.repository.AddressSnapshotRepository;
import ch.grignola.repository.UserCollectionAddressRepository;
import ch.grignola.service.balance.AddressBalance;
import ch.grignola.service.balance.AddressBalanceChecker;
import ch.grignola.service.balance.TokenBalance;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@ApplicationScoped
public class TakeAddressSnapshotsJob {

    private static final Logger LOG = Logger.getLogger(TakeAddressSnapshotsJob.class);

    @Inject
    AddressBalanceChecker addressBalanceChecker;

    @Inject
    AddressSnapshotRepository addressSnapshotRepository;

    @Inject
    UserCollectionAddressRepository userCollectionAddressRepository;

    @Scheduled(cron = "0 0 0/3 * * ?")
    @Transactional
    void doSnapshots() {
        userCollectionAddressRepository.listAll().stream()
                .map(UserCollectionAddress::getAddress)
                .distinct()
                .forEach(this::snapshotAddress);
    }

    private void snapshotAddress(String address) {
        try {
            AddressBalance addressBalance = addressBalanceChecker.getAddressBalance(address);
            AddressSnapshot snapshot = new AddressSnapshot();
            snapshot.setAddress(address);
            snapshot.setDateTime(OffsetDateTime.now());
            snapshot.setUsdValue(addressBalance.tokenBalances().stream()
                    .map(TokenBalance::getUsdValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            addressSnapshotRepository.persist(snapshot);

            LOG.infof("Snapshot done for address %s, value is %s USD", address, snapshot.getUsdValue());
        } catch (Exception ex) {
            LOG.errorf(ex, "Snapshot error for address %s", address);
        }

    }
}
