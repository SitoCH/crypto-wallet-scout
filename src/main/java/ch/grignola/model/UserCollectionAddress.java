package ch.grignola.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class UserCollectionAddress {
    private Long id;
    private UserCollection userCollection;
    private String address;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public UserCollection getUserCollection() {
        return userCollection;
    }

    public void setUserCollection(UserCollection userCollection) {
        this.userCollection = userCollection;
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
        UserCollectionAddress that = (UserCollectionAddress) o;
        return Objects.equals(id, that.id) && Objects.equals(userCollection, that.userCollection) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userCollection, address);
    }
}
