package ch.grignola.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
public class AddressSnapshot {
    private Long id;
    private OffsetDateTime dateTime;
    private BigDecimal usdValue;
    private String address;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Column(precision = 30, scale = 10)
    public BigDecimal getUsdValue() {
        return usdValue;
    }

    public void setUsdValue(BigDecimal usdValue) {
        this.usdValue = usdValue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddressSnapshot snapshot = (AddressSnapshot) o;
        return Objects.equals(id, snapshot.id) && Objects.equals(dateTime, snapshot.dateTime) && Objects.equals(usdValue, snapshot.usdValue) && Objects.equals(address, snapshot.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, usdValue, address);
    }
}
