package bg.softuni.onlinepharmacy.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    private Set<Drug> orderedDrugs;
    private LocalDate orderDate;

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Drug> getOrderedDrugs() {
        return orderedDrugs;
    }

    public void setOrderedDrugs(Set<Drug> orderedDrugs) {
        this.orderedDrugs = orderedDrugs;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
