package bg.softuni.onlinepharmacy.model.entity;


import jakarta.persistence.*;
import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "users")
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

    public Drug() {
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
}
