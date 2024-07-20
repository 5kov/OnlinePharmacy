package bg.softuni.onlinepharmacy.model.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "interactions")
public class Interaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(optional = false)
    private ActiveIngredient drugName;
    @ManyToOne(optional = false)
    private ActiveIngredient interactionDrug;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InteractionTypeEnum interactionType;

    public Interaction() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ActiveIngredient getDrugName() {
        return drugName;
    }

    public void setDrugName(ActiveIngredient drugName) {
        this.drugName = drugName;
    }

    public ActiveIngredient getInteractionDrug() {
        return interactionDrug;
    }

    public void setInteractionDrug(ActiveIngredient interactionDrug) {
        this.interactionDrug = interactionDrug;
    }

    public InteractionTypeEnum getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(InteractionTypeEnum interactionType) {
        this.interactionType = interactionType;
    }
}
