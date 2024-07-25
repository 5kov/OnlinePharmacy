package bg.softuni.onlinepharmacy.config;


import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {
    private long id;
    private String username;
    private boolean isAdmin;

    public boolean isLoggedIn() {
        return id != 0;
    }

    public void logIn(UserEntity userEntity) {
        this.id = userEntity.getId();
        this.username = userEntity.getUsername();
        this.isAdmin = userEntity.isAdministrator();
    }

    public void logOut() {
        this.id = 0;
        this.username = "";
        this.isAdmin = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
