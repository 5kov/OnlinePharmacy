package bg.softuni.onlinepharmacy.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "active_ingredients")
public class ActiveIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String ingredientName;

    @Column(unique = true, nullable = false)
    private String ingredientCode;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientCode() {
        return ingredientCode;
    }

    public void setIngredientCode(String ingredientCode) {
        this.ingredientCode = ingredientCode;
    }
}
