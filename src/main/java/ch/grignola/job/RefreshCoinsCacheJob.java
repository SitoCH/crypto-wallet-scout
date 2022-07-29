package ch.grignola.job;

import ch.grignola.service.token.TokenProvider;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class RefreshCoinsCacheJob {

    private static final Logger LOG = Logger.getLogger(RefreshCoinsCacheJob.class);

    @Inject
    TokenProvider tokenProvider;

    @Transactional
    @Scheduled(every = "5m")
    void refreshCoinsCache() {
        tokenProvider.refreshCache();
        LOG.infof("Refreshed token cache");
    }
}
