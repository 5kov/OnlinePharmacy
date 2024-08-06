package bg.softuni.onlinepharmacy.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddNewsDTO {
    @NotBlank
    @Size(min = 10, max = 100)
    private String titleEn;
    @NotBlank
    @Size(min = 10, max = 100)
    private String titleBg;
    @NotBlank
    @Size(min = 10, max = 5000)
    private String contentEn;
    @NotBlank
    @Size(min = 10, max = 5000)
    private String contentBg;

    public AddNewsDTO() {
    }

    public @NotBlank @Size(min = 10, max = 100) String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(@NotBlank @Size(min = 10, max = 100) String titleEn) {
        this.titleEn = titleEn;
    }

    public @NotBlank @Size(min = 10, max = 100) String getTitleBg() {
        return titleBg;
    }

    public void setTitleBg(@NotBlank @Size(min = 10, max = 100) String titleBg) {
        this.titleBg = titleBg;
    }

    public @NotBlank @Size(min = 10, max = 5000) String getContentEn() {
        return contentEn;
    }

    public void setContentEn(@NotBlank @Size(min = 10, max = 5000) String contentEn) {
        this.contentEn = contentEn;
    }

    public @NotBlank @Size(min = 10, max = 5000) String getContentBg() {
        return contentBg;
    }

    public void setContentBg(@NotBlank @Size(min = 10, max = 5000) String contentBg) {
        this.contentBg = contentBg;
    }
}
