package bg.softuni.onlinepharmacy.repository;

import bg.softuni.onlinepharmacy.model.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CartItemRepositoryIntegrationTest {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private ActiveIngredientRepository activeIngredientRepository;

    @Autowired
    private UserRepository userRepository;

    private Cart cart;
    private Medicine medicine;
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        userEntity.setUsername("john_doe");
        userEntity.setUuid(UUID.randomUUID());
        userEntity.setFirstName("John");
        userEntity.setLastName("Doe");
        userEntity.setEmail("john.doe@example.com");
        userEntity.setCountry("USA");
        userEntity.setCity("New York");
        userEntity.setStreet("123 Elm Street");
        userEntity.setPostalCode("10001");
        userEntity.setPhoneNumber("1234567890");
        userEntity.setPassword("password");
        userEntity.setAdministrator(false);
        userEntity = userRepository.save(userEntity);

        cart = new Cart();
        cart.setUser(userEntity);
        cart = cartRepository.save(cart);

        ActiveIngredient activeIngredient = new ActiveIngredient();
        activeIngredient.setIngredientName("Test ingredient");
        activeIngredient.setIngredientCode("Test");
        activeIngredientRepository.save(activeIngredient);

        medicine = new Medicine();
        medicine.setMedicineNameEn("Test 500mg");
        medicine.setMedicineNameBg("Тест 500мг");
        medicine.setPrice(5.99);
        medicine.setDescriptionEn("Test is used to reduce fever and relieve mild to moderate pain.");
        medicine.setDescriptionBg("Тест се използва за намаляване на температурата и облекчаване на лека до умерена болка.");
        medicine.setImageUrl("test.png");
        medicine.setFavourite(false);
        medicine.setVotes(10);
        medicine.setActiveIngredient(activeIngredient);
        medicineRepository.save(medicine);


        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setMedicine(medicine);
        cartItem.setQuantity(1);
        cartItem.setItemPrice(10.0);
        cartItemRepository.save(cartItem);
    }

    @Test
    public void testFindCartItemByCartIdAndMedicineId_ShouldReturnCartItem() {
        // Act
        Optional<CartItem> result = cartItemRepository.findCartItemByCartIdAndMedicineId(cart.getId(), medicine.getId());

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getCart().getId()).isEqualTo(cart.getId());
        assertThat(result.get().getMedicine().getId()).isEqualTo(medicine.getId());
    }

    @Test
    public void testFindCartItemByCartIdAndMedicineId_ShouldReturnEmpty() {
        // Act
        Optional<CartItem> result = cartItemRepository.findCartItemByCartIdAndMedicineId(cart.getId(), 999L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    public void testSaveCartItem() {
        // Arrange
        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setMedicine(medicine);
        newCartItem.setQuantity(2);
        newCartItem.setItemPrice(20.0);

        // Act
        CartItem savedCartItem = cartItemRepository.save(newCartItem);

        // Assert
        assertThat(savedCartItem).isNotNull();
        assertThat(savedCartItem.getId()).isNotNull();
        assertThat(savedCartItem.getQuantity()).isEqualTo(2);
        assertThat(savedCartItem.getItemPrice()).isEqualTo(20.0);
    }
}
