package bg.softuni.onlinepharmacy.repository;



import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    @Query("SELECT COUNT(u) FROM UserEntity u")
    long countUsers();

    @Query("SELECT u FROM UserEntity u WHERE u.username LIKE %:username%")
    List<UserEntity> findByUsernameContaining(@Param("username") String username);


    @Transactional
    @Modifying
    @Query("DELETE FROM UserEntity u WHERE u.id = :id")
    void deleteUserById(@Param("id") long id);

    @Transactional
    @Modifying
    @Query("UPDATE UserEntity u SET u.administrator = :adminStatus WHERE u.id = :id")
    void updateUserAdminStatus(@Param("id") Long id, @Param("adminStatus") boolean adminStatus);

    long countByAdministrator(boolean b);

}
