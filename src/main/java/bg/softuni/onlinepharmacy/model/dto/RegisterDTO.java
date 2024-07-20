package bg.softuni.onlinepharmacy.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterDTO {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 3, max = 20)
    private String password;
    @NotBlank
    @Size(min = 3, max = 20)
    private String confirmPassword;

    public RegisterDTO() {
    }

    public @NotBlank @Size(min = 3, max = 20) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(min = 3, max = 20) String username) {
        this.username = username;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 3, max = 20) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 3, max = 20) String password) {
        this.password = password;
    }

    public @NotBlank @Size(min = 3, max = 20) String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank @Size(min = 3, max = 20) String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
