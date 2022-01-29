package ch.grignola.job;

import ch.grignola.repository.AddressSnapshotRepository;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@ApplicationScoped
public class CleanAddressSnapshotsJob {

    private static final Logger LOG = Logger.getLogger(CleanAddressSnapshotsJob.class);

    @Inject
    AddressSnapshotRepository addressSnapshotRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    void cleanOldSnapshots() {
        OffsetDateTime discardUntilDateTime = OffsetDateTime.now().minusDays(30);
        addressSnapshotRepository.deleteBeforeDateTime(discardUntilDateTime);
        LOG.infof("Cleaned old snapshots up to %s", discardUntilDateTime);
    }
}
