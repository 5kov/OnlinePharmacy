package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;

    @BeforeEach
    public void setUp() {
        // Create and save a UserEntity
        user = new UserEntity();
        user.setUsername("testuser");
        user.setUuid(UUID.randomUUID());
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("testuser@example.com");
        user.setCountry("TestCountry");
        user.setCity("TestCity");
        user.setStreet("123 Test Street");
        user.setPostalCode("12345");
        user.setPhoneNumber("1234567890");
        user.setPassword("password");
        user.setAdministrator(false);
        userRepository.deleteAll();
        userRepository.save(user);
    }

    @Test
    public void testFindByUsername_ShouldReturnUser() {
        // Act
        Optional<UserEntity> foundUser = userRepository.findByUsername("testuser");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
        assertThat(foundUser.get().getEmail()).isEqualTo("testuser@example.com");
        assertThat(foundUser.get().getFirstName()).isEqualTo("Test");
        assertThat(foundUser.get().getLastName()).isEqualTo("User");
        assertThat(foundUser.get().getCountry()).isEqualTo("TestCountry");
        assertThat(foundUser.get().getCity()).isEqualTo("TestCity");
        assertThat(foundUser.get().getStreet()).isEqualTo("123 Test Street");
        assertThat(foundUser.get().getPostalCode()).isEqualTo("12345");
        assertThat(foundUser.get().getPhoneNumber()).isEqualTo("1234567890");
    }

    @Test
    public void testFindByUsername_ShouldReturnEmpty() {
        // Act
        Optional<UserEntity> foundUser = userRepository.findByUsername("nonexistentuser");

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    public void testFindByEmail_ShouldReturnUser() {
        // Act
        Optional<UserEntity> foundUser = userRepository.findByEmail("testuser@example.com");

        // Assert
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("testuser@example.com");
        assertThat(foundUser.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindByEmail_ShouldReturnEmpty() {
        // Act
        Optional<UserEntity> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertThat(foundUser).isEmpty();
    }

    @Test
    public void testCountUsers_ShouldReturnCorrectCount() {
        // Act
        long userCount = userRepository.countUsers();

        // Assert
        assertThat(userCount).isEqualTo(1);
    }

    @Test
    public void testFindByUsernameContaining_ShouldReturnMatchingUsers() {
        // Act
        List<UserEntity> users = userRepository.findByUsernameContaining("test");

        // Assert
        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getUsername()).isEqualTo("testuser");
    }

    @Test
    public void testFindByUsernameContaining_ShouldReturnEmptyList() {
        // Act
        List<UserEntity> users = userRepository.findByUsernameContaining("nonexistent");

        // Assert
        assertThat(users).isEmpty();
    }

    @Test
    public void testDeleteUserById_ShouldRemoveUser() {
        // Arrange
        long userId = user.getId();
        System.out.println("id of user" + userId);

        // Act
        userRepository.deleteById(userId);
        Optional<UserEntity> deletedUser = userRepository.findById(userId);

        // Assert
        assertThat(deletedUser).isEmpty();
    }

}
