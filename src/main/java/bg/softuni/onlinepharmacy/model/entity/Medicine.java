package bg.softuni.onlinepharmacy.model.entity;


import jakarta.persistence.*;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Entity
@Table(name = "medicines")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String medicineNameEn;
    @Column(nullable = false)
    private String medicineNameBg;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private String descriptionEn;
    @Column(nullable = false)
    private String descriptionBg;
    @Column(nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private boolean isFavourite;
    @Column(nullable = false)
    private int votes;
    @ManyToOne
    @JoinColumn(name = "active_ingredient_id", nullable = false)
    private ActiveIngredient activeIngredient;
    @OneToMany(mappedBy = "medicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    public Medicine() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedicineNameEn() {
        return medicineNameEn;
    }

    public void setMedicineNameEn(String medicineNameEn) {
        this.medicineNameEn = medicineNameEn;
    }

    public String getMedicineNameBg() {
        return medicineNameBg;
    }

    public void setMedicineNameBg(String medicineNameBg) {
        this.medicineNameBg = medicineNameBg;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getDescriptionBg() {
        return descriptionBg;
    }

    public void setDescriptionBg(String descriptionBg) {
        this.descriptionBg = descriptionBg;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public ActiveIngredient getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(ActiveIngredient activeIngredient) {
        this.activeIngredient = activeIngredient;
    }
}
