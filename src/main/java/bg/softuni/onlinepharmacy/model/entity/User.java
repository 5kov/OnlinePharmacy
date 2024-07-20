package bg.softuni.onlinepharmacy.model.entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    @ManyToMany
    private Set<Drug> favouriteDrugs;
    @ManyToMany
    private Set<Drug> ratedDrugs;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    public User() {
        this.favouriteDrugs = new HashSet<>();
        this.ratedDrugs = new HashSet<>();
        this.orders = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Drug> getFavouriteDrugs() {
        return favouriteDrugs;
    }

    public void setFavouriteDrugs(Set<Drug> favouriteDrugs) {
        this.favouriteDrugs = favouriteDrugs;
    }

    public Set<Drug> getRatedDrugs() {
        return ratedDrugs;
    }

    public void setRatedDrugs(Set<Drug> ratedDrugs) {
        this.ratedDrugs = ratedDrugs;
    }
}
