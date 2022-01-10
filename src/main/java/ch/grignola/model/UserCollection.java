package ch.grignola.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserCollection {
    private Long id;
    private User user;
    private String name;
    private Set<UserCollectionAddress> userCollectionAddresses = new HashSet<>();

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


    public void addUserCollectionAddresses(UserCollectionAddress userCollectionAddress) {
        userCollectionAddresses.add(userCollectionAddress);
        userCollectionAddress.setUserCollection(this);
    }

    public void removeUserCollectionAddresses(UserCollectionAddress userCollectionAddress) {
        userCollectionAddresses.remove(userCollectionAddress);
        userCollectionAddress.setUserCollection(null);
    }

    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "userCollection",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    public Set<UserCollectionAddress> getUserCollectionAddresses() {
        return userCollectionAddresses;
    }

    public void setUserCollectionAddresses(Set<UserCollectionAddress> userCollectionAddresses) {
        this.userCollectionAddresses = userCollectionAddresses;
    }
}
