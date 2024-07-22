package bg.softuni.onlinepharmacy.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ActiveIngredientDTO {
    @NotBlank(message = "The name cannot be empty.")
    @Size(min = 3, max = 20, message = "The name must be between 3 and 20 characters.")
    private String ingredientName;

    @NotBlank(message = "The code cannot be empty.")
    @Size(min = 1, max = 6, message = "The code must be between 1 and 6 characters.")
    private String ingredientCode;

    // Getters and Setters
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
