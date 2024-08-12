package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.Cart;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartRepositoryIntegrationTest {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;
    private Cart cart;

    @BeforeEach
    public void setUp() {
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
        userRepository.save(user);  // Save the user first

        cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Test
    @Transactional
    public void testFindByUserEntity_ShouldReturnCart() {
        // Act
        Cart foundCart = cartRepository.findByUserEntity(user);

        // Assert
        assertThat(foundCart).isNotNull();
        assertThat(foundCart.getUser()).isEqualTo(user);
    }


    @Test
    @Transactional
    public void testSaveCart() {
        // Act
        Cart savedCart = cartRepository.save(cart);

        // Assert
        assertThat(savedCart).isNotNull();
        assertThat(savedCart.getId()).isNotNull();
        assertThat(savedCart.getUser()).isEqualTo(user);
    }
}
