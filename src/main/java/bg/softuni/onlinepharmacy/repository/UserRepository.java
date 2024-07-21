package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();
}
