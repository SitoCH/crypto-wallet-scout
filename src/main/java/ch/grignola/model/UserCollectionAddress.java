package ch.grignola.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserCollectionAddress {
    private Long id;
    private UserCollection userCollection;
    private String address;

    @Id
    @GeneratedValue
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
}
