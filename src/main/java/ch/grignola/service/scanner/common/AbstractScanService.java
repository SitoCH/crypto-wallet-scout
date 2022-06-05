package ch.grignola.service.scanner.common;

import ch.grignola.model.ContractVerificationStatus;
import ch.grignola.model.Network;
import ch.grignola.repository.ContractVerificationStatusRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static ch.grignola.model.ContractVerificationStatus.Status.BANNED;
import static ch.grignola.model.ContractVerificationStatus.Status.UNKNOWN;
import static java.util.stream.Collectors.toUnmodifiableSet;

public abstract class AbstractScanService {

    private static final Logger LOG = Logger.getLogger(AbstractScanService.class);

    @Inject
    ContractVerificationStatusRepository contractVerificationStatusRepository;


    protected void checkContractVerificationStatus(Set<String> allVerifiedContracts, Network network, String contractAddress) {
        if (contractAddress.startsWith("0x") && !allVerifiedContracts.contains(contractAddress)) {
            ContractVerificationStatus contractVerificationStatus = new ContractVerificationStatus();
            contractVerificationStatus.setNetwork(network);
            contractVerificationStatus.setContractId(contractAddress);
            contractVerificationStatus.setStatus(UNKNOWN);
            contractVerificationStatusRepository.persist(contractVerificationStatus);
            LOG.infof("Contract status for %s and network %s has been flagged as UNKNOWN", contractAddress, network);
        }
    }

    protected ContractStatus getContractStatus(Network network) {
        List<ContractVerificationStatus> contracts = contractVerificationStatusRepository.findByNetwork(network);
        Set<String> allVerifiedContracts = contracts.stream()
                .map(ContractVerificationStatus::getContractId)
                .collect(toUnmodifiableSet());
        Set<String> bannedContracts = contracts.stream()
                .filter(x -> x.getStatus() == BANNED)
                .map(ContractVerificationStatus::getContractId)
                .collect(toUnmodifiableSet());

        return new ContractStatus(allVerifiedContracts, bannedContracts);
    }

    public record ContractStatus(Set<String> allVerifiedContracts, Set<String> bannedContracts) {
    }
}
