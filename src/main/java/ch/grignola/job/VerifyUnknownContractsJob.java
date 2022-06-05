package ch.grignola.job;

import ch.grignola.model.ContractVerificationStatus;
import ch.grignola.repository.ContractVerificationStatusRepository;
import ch.grignola.service.token.TokenProvider;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import static ch.grignola.model.ContractVerificationStatus.Status.*;

@ApplicationScoped
public class VerifyUnknownContractsJob {

    private static final Logger LOG = Logger.getLogger(VerifyUnknownContractsJob.class);


    @Inject
    ContractVerificationStatusRepository contractVerificationStatusRepository;

    @Inject
    TokenProvider tokenProvider;

    //@Scheduled(cron = "0 0/30 * * * ?")
    @Scheduled(every = "60s")
    @Transactional
    void verifyContracts() {
        contractVerificationStatusRepository.findByStatus(UNKNOWN)
                .forEach(this::verifyContract);
    }

    private void verifyContract(ContractVerificationStatus contractStatus) {
        tokenProvider.getContract(contractStatus.getNetwork(), contractStatus.getContractId())
                .ifPresentOrElse(contract -> {
                    contractStatus.setStatus(contract.liquidityScore == 0 ? BANNED : VERIFIED);
                    LOG.infof("Contract %s for token %s on %s status set to %s", contractStatus.getContractId(), contract.name, contractStatus.getNetwork(), contractStatus.getStatus());
                    contractVerificationStatusRepository.persist(contractStatus);
                }, () -> LOG.infof("Contract %s on %s not found", contractStatus.getContractId(), contractStatus.getNetwork()));
    }
}
