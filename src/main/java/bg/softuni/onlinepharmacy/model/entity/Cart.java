package bg.softuni.onlinepharmacy.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToMany(mappedBy = "cart")
    private Set<User> users;
    @ManyToMany
    private Set<Drug> drugsInCart;
    public Cart() {
        this.users = new HashSet<>();
        this.drugsInCart = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Drug> getDrugsInCart() {
        return drugsInCart;
    }

    public void setDrugsInCart(Set<Drug> drugsInCart) {
        this.drugsInCart = drugsInCart;
    }
}
