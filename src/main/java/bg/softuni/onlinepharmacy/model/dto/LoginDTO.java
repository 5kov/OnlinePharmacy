package bg.softuni.onlinepharmacy.model.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginDTO {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Size(min = 3, max = 20)
    private String password;

    public LoginDTO() {
    }

    public @NotBlank @Size(min = 3, max = 20) String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank @Size(min = 3, max = 20) String username) {
        this.username = username;
    }

    public @NotBlank @Size(min = 3, max = 20) String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank @Size(min = 3, max = 20) String password) {
        this.password = password;
    }
}
