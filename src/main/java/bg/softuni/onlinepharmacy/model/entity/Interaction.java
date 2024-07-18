package bg.softuni.onlinepharmacy.model.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String drugName;
    @Column(nullable = false)
    private String interactionDrug;
    @Column(nullable = false)
    private String interactionType;

    public Interaction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getInteractionDrug() {
        return interactionDrug;
    }

    public void setInteractionDrug(String interactionDrug) {
        this.interactionDrug = interactionDrug;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }
}
