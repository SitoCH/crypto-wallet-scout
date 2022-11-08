package ch.grignola.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class AddressTokenValue {
    private Long id;
    private Network network;
    private String address;
    private OffsetDateTime dateTime;
    private String tokenSymbol;
    private String tokenContract;
    private BigDecimal nativeValue;


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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    @Column(precision = 30, scale = 10)
    public BigDecimal getNativeValue() {
        return nativeValue;
    }

    public void setNativeValue(BigDecimal nativeValue) {
        this.nativeValue = nativeValue;
    }

    public String getTokenContract() {
        return tokenContract;
    }

    public void setTokenContract(String tokenContract) {
        this.tokenContract = tokenContract;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddressTokenValue that = (AddressTokenValue) o;
        return Objects.equals(id, that.id) && network == that.network && Objects.equals(address, that.address) && Objects.equals(dateTime, that.dateTime) && Objects.equals(tokenSymbol, that.tokenSymbol) && Objects.equals(tokenContract, that.tokenContract) && Objects.equals(nativeValue, that.nativeValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, network, address, dateTime, tokenSymbol, tokenContract, nativeValue);
    }
}
