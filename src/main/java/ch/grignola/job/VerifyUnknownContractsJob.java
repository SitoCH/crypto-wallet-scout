package ch.grignola.job;

import ch.grignola.model.ContractVerificationStatus;
import ch.grignola.repository.ContractVerificationStatusRepository;
import ch.grignola.service.token.TokenContract;
import ch.grignola.service.token.TokenContractStatus;
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

    @Scheduled(cron = "0 0/30 * * * ?")
    @Transactional
    void verifyContracts() {
        contractVerificationStatusRepository.findByStatus(UNKNOWN)
                .forEach(this::verifyContract);
    }

    private void verifyContract(ContractVerificationStatus contractStatus) {
        TokenContract tokenContract = tokenProvider.getContract(contractStatus.getNetwork(), contractStatus.getContractId());
        if (tokenContract.status() == TokenContractStatus.VERIFIED) {
            setStatus(contractStatus, VERIFIED, tokenContract);
        } else if (tokenContract.status() == TokenContractStatus.BANNED) {
            setStatus(contractStatus, BANNED, tokenContract);
        } else if (tokenContract.status() == TokenContractStatus.CONTRACT_NOT_FOUND) {
            setStatus(contractStatus, NOT_FOUND, tokenContract);
        } else {
            LOG.warnf("Contract %s on %s can't be processed: %s", contractStatus.getContractId(), contractStatus.getNetwork(), tokenContract.status());
        }
    }

    private void setStatus(ContractVerificationStatus contractStatus, ContractVerificationStatus.Status banned, TokenContract tokenContract) {
        contractStatus.setStatus(banned);
        LOG.infof("Contract %s for token %s on %s status set to %s", contractStatus.getContractId(), tokenContract.name(), contractStatus.getNetwork(), contractStatus.getStatus());
        contractVerificationStatusRepository.persist(contractStatus);
    }
}
