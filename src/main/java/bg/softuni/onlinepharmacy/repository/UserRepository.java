package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();
    @Query("SELECT u FROM User u WHERE u.username LIKE %:username%")
    List<User> findByUsernameContaining(@Param("username") String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id = :id")
    void deleteUserById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.administrator = :adminStatus WHERE u.id = :id")
    void updateUserAdminStatus(@Param("id") Long id, @Param("adminStatus") boolean adminStatus);

    long countByAdministrator(boolean b);

}
