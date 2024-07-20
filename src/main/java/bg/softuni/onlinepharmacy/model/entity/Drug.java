package bg.softuni.onlinepharmacy.model.entity;


import jakarta.persistence.*;
import org.hibernate.validator.constraints.URL;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "drugs")
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String drugNameEn;
    @Column(nullable = false)
    private String drugNameBg;
    @Column(nullable = false)
    private String activeIngredient;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private String descriptionEn;
    @Column(nullable = false)
    private String descriptionBg;
    @Column(nullable = false)
    @URL
    private String imageUrl;
    @Column(nullable = false)
    private boolean isFavourite;
    @Column(nullable = false)
    private int votes;
    @ManyToMany
    private Set<ActiveIngredient> activeIngredients;

    public Drug() {
        this.activeIngredients = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDrugNameEn() {
        return drugNameEn;
    }

    public void setDrugNameEn(String drugNameEn) {
        this.drugNameEn = drugNameEn;
    }

    public String getDrugNameBg() {
        return drugNameBg;
    }

    public void setDrugNameBg(String drugNameBg) {
        this.drugNameBg = drugNameBg;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
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

    public @URL String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@URL String imageUrl) {
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

    public Set<ActiveIngredient> getActiveIngredients() {
        return activeIngredients;
    }

    public void setActiveIngredients(Set<ActiveIngredient> activeIngredients) {
        this.activeIngredients = activeIngredients;
    }
}
