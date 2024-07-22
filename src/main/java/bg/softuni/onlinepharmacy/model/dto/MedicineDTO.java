package bg.softuni.onlinepharmacy.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

public class MedicineDTO {
    @NotBlank(message = "The name in English must not be blank.")
    @Size(min = 3, max = 20, message = "The name in English must be between 3 and 20 characters.")
    private String medicineNameEn;

    @NotBlank(message = "The name in Bulgarian must not be blank.")
    @Size(min = 3, max = 20, message = "The name in Bulgarian must be between 3 and 20 characters.")
    private String medicineNameBg;

    @Positive(message = "Price must be a positive number.")
    private double price;

    @NotBlank(message = "The description in English must not be blank.")
    @Size(min = 3, max = 20, message = "The description in English must be between 3 and 20 characters.")
    private String descriptionEn;

    @NotBlank(message = "The description in Bulgarian must not be blank.")
    @Size(min = 3, max = 20, message = "The description in Bulgarian must be between 3 and 20 characters.")
    private String descriptionBg;

    @NotBlank(message = "The image url must not be blank.")
    @Size(min = 3, max = 100, message = "The image url must be between 3 and 100 characters.")
    private String imageUrl;
    @NotNull
    private Long activeIngredientId;

    public MedicineDTO() {
    }

    public @NotBlank(message = "The name in English must not be blank.") @Size(min = 3, max = 20, message = "The name in English must be between 3 and 20 characters.") String getMedicineNameEn() {
        return medicineNameEn;
    }

    public void setMedicineNameEn(@NotBlank(message = "The name in English must not be blank.") @Size(min = 3, max = 20, message = "The name in English must be between 3 and 20 characters.") String medicineNameEn) {
        this.medicineNameEn = medicineNameEn;
    }

    public @NotBlank(message = "The name in Bulgarian must not be blank.") @Size(min = 3, max = 20, message = "The name in Bulgarian must be between 3 and 20 characters.") String getMedicineNameBg() {
        return medicineNameBg;
    }

    public void setMedicineNameBg(@NotBlank(message = "The name in Bulgarian must not be blank.") @Size(min = 3, max = 20, message = "The name in Bulgarian must be between 3 and 20 characters.") String medicineNameBg) {
        this.medicineNameBg = medicineNameBg;
    }

    @Positive(message = "Price must be a positive number.")
    public double getPrice() {
        return price;
    }

    public void setPrice(@Positive(message = "Price must be a positive number.") double price) {
        this.price = price;
    }

    public @NotBlank(message = "The description in English must not be blank.") @Size(min = 3, max = 20, message = "The description in English must be between 3 and 20 characters.") String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(@NotBlank(message = "The description in English must not be blank.") @Size(min = 3, max = 20, message = "The description in English must be between 3 and 20 characters.") String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public @NotBlank(message = "The description in Bulgarian must not be blank.") @Size(min = 3, max = 20, message = "The description in Bulgarian must be between 3 and 20 characters.") String getDescriptionBg() {
        return descriptionBg;
    }

    public void setDescriptionBg(@NotBlank(message = "The description in Bulgarian must not be blank.") @Size(min = 3, max = 20, message = "The description in Bulgarian must be between 3 and 20 characters.") String descriptionBg) {
        this.descriptionBg = descriptionBg;
    }

    public @NotBlank(message = "The image url must not be blank.") @Size(min = 3, max = 100, message = "The image url must be between 3 and 100 characters.") String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@NotBlank(message = "The image url must not be blank.") @Size(min = 3, max = 100, message = "The image url must be between 3 and 100 characters.") String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public @NotNull Long getActiveIngredientId() {
        return activeIngredientId;
    }

    public void setActiveIngredientId(@NotNull Long activeIngredientId) {
        this.activeIngredientId = activeIngredientId;
    }
}
