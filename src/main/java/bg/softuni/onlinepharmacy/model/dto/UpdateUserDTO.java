package bg.softuni.onlinepharmacy.model.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserDTO {
    private long id;
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 3, max = 20)
    private String country;
    @NotBlank
    @Size(min = 3, max = 20)
    private String city;
    @NotBlank
    @Size(min = 3, max = 40)
    private String street;
    @NotBlank
    @Size(min = 3, max = 5)
    private String postalCode;
    @NotBlank
    @Size(min = 3, max = 20)
    private String phoneNumber;
    @Size(min = 3, max = 20)
    private String password;
    @Size(min = 3, max = 20)
    private String confirmPassword;

    public UpdateUserDTO() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public @NotBlank @Size(min = 3, max = 20) String getCountry() {
        return country;
    }

    public void setCountry(@NotBlank @Size(min = 3, max = 20) String country) {
        this.country = country;
    }

    public @NotBlank @Size(min = 3, max = 20) String getCity() {
        return city;
    }

    public void setCity(@NotBlank @Size(min = 3, max = 20) String city) {
        this.city = city;
    }

    public @NotBlank @Size(min = 3, max = 40) String getStreet() {
        return street;
    }

    public void setStreet(@NotBlank @Size(min = 3, max = 40) String street) {
        this.street = street;
    }

    public @NotBlank @Size(min = 3, max = 5) String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(@NotBlank @Size(min = 3, max = 5) String postalCode) {
        this.postalCode = postalCode;
    }

    public @NotBlank @Size(min = 3, max = 20) String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotBlank @Size(min = 3, max = 20) String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @Size(min = 3, max = 20) String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 3, max = 20) String password) {
        this.password = password;
    }

    public @Size(min = 3, max = 20) String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@Size(min = 3, max = 20) String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
