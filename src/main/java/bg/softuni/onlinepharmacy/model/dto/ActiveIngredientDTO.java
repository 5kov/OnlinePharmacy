package bg.softuni.onlinepharmacy.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ActiveIngredientDTO {
    @NotBlank(message = "The name cannot be empty.")
    @Size(min = 3, max = 20, message = "The name must be between 3 and 20 characters.")
    private String ingredientName;

    @NotBlank(message = "The code cannot be empty.")
    @Size(min = 1, max = 7, message = "The code must be between 1 and 7 characters.")
    private String ingredientCode;

    public ActiveIngredientDTO() {
    }

    public @NotBlank(message = "The name cannot be empty.") @Size(min = 3, max = 20, message = "The name must be between 3 and 20 characters.") String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(@NotBlank(message = "The name cannot be empty.") @Size(min = 3, max = 20, message = "The name must be between 3 and 20 characters.") String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public @NotBlank(message = "The code cannot be empty.") @Size(min = 1, max = 7, message = "The code must be between 1 and 7 characters.") String getIngredientCode() {
        return ingredientCode;
    }

    public void setIngredientCode(@NotBlank(message = "The code cannot be empty.") @Size(min = 1, max = 7, message = "The code must be between 1 and 7 characters.") String ingredientCode) {
        this.ingredientCode = ingredientCode;
    }
}