package ch.grignola.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class UserCollection {
    private Long id;
    private User user;
    private String name;

    private Set<UserCollectionAddress> userCollectionAddresses;

    public UserCollection() {
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userCollection")
    public Set<UserCollectionAddress> getUserCollectionAddresses() {
        return userCollectionAddresses;
    }

    public void setUserCollectionAddresses(Set<UserCollectionAddress> userCollectionAddresses) {
        this.userCollectionAddresses = userCollectionAddresses;
    }
}
