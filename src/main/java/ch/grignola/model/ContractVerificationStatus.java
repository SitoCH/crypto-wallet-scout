package ch.grignola.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ContractVerificationStatus {
    private Long id;
    private Network network;
    private String contractId;
    private Status status;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    @Enumerated(EnumType.STRING)
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractVerificationStatus that = (ContractVerificationStatus) o;
        return Objects.equals(id, that.id) && network == that.network && Objects.equals(contractId, that.contractId) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, network, contractId, status);
    }

    public enum Status {
        BANNED, VERIFIED, UNKNOWN
    }
}
