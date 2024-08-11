package bg.softuni.onlinepharmacy.service.impl;

import bg.softuni.onlinepharmacy.model.entity.*;
import bg.softuni.onlinepharmacy.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OrderServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private InteractionRepository interactionRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderServiceImpl orderService;

    private UserEntity user;
    private Cart cart;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock SecurityContextHolder
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Set up mock user and cart
        user = new UserEntity();
        user.setUsername("testUser");
        cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new ArrayList<>());
        user.setCart(cart);

        // Set up mocks for repositories
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
    }

    @Test
    void placeOrder_Successful() {
        // Prepare cart items and medicines
        Medicine medicine = new Medicine();
        ActiveIngredient ingredient = new ActiveIngredient();
        medicine.setActiveIngredient(ingredient);

        CartItem cartItem = new CartItem();
        cartItem.setMedicine(medicine);
        cartItem.setQuantity(2);
        cartItem.setItemPrice(100.0);

        cart.getCartItems().add(cartItem);

        when(interactionRepository.existsByActiveIngredients(any(), any())).thenReturn(false);

        // Perform the order placement
        boolean result = orderService.placeOrder();

        assertTrue(result);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(cartRepository, times(1)).save(cart);
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void placeOrder_EmptyCart() {
        // Cart is already empty in the setup
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            orderService.placeOrder();
        });

        assertEquals("Cannot place an order because the cart is empty", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void placeOrder_WithInteractions() {
        // Prepare cart items and medicines
        Medicine medicine1 = new Medicine();
        ActiveIngredient ingredient1 = new ActiveIngredient();
        medicine1.setActiveIngredient(ingredient1);

        Medicine medicine2 = new Medicine();
        ActiveIngredient ingredient2 = new ActiveIngredient();
        medicine2.setActiveIngredient(ingredient2);

        CartItem cartItem1 = new CartItem();
        cartItem1.setMedicine(medicine1);
        cartItem1.setQuantity(2);
        cartItem1.setItemPrice(100.0);

        CartItem cartItem2 = new CartItem();
        cartItem2.setMedicine(medicine2);
        cartItem2.setQuantity(1);
        cartItem2.setItemPrice(50.0);

        cart.getCartItems().add(cartItem1);
        cart.getCartItems().add(cartItem2);

        when(interactionRepository.existsByActiveIngredients(ingredient1, ingredient2)).thenReturn(true);

        // Perform the order placement
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            orderService.placeOrder();
        });

        assertEquals("Cannot place order due to incompatible ingredients", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void checkForInteractions_WithInteraction() {
        // Prepare cart items and medicines
        Medicine medicine1 = new Medicine();
        ActiveIngredient ingredient1 = new ActiveIngredient();
        medicine1.setActiveIngredient(ingredient1);

        Medicine medicine2 = new Medicine();
        ActiveIngredient ingredient2 = new ActiveIngredient();
        medicine2.setActiveIngredient(ingredient2);

        CartItem cartItem1 = new CartItem();
        cartItem1.setMedicine(medicine1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setMedicine(medicine2);

        cart.getCartItems().add(cartItem1);
        cart.getCartItems().add(cartItem2);

        when(interactionRepository.existsByActiveIngredients(ingredient1, ingredient2)).thenReturn(true);

        boolean result = orderService.checkForInteractions(cart);
        assertTrue(result);
    }

    @Test
    void checkForInteractions_WithoutInteraction() {
        // Prepare cart items and medicines
        Medicine medicine1 = new Medicine();
        ActiveIngredient ingredient1 = new ActiveIngredient();
        medicine1.setActiveIngredient(ingredient1);

        Medicine medicine2 = new Medicine();
        ActiveIngredient ingredient2 = new ActiveIngredient();
        medicine2.setActiveIngredient(ingredient2);

        CartItem cartItem1 = new CartItem();
        cartItem1.setMedicine(medicine1);

        CartItem cartItem2 = new CartItem();
        cartItem2.setMedicine(medicine2);

        cart.getCartItems().add(cartItem1);
        cart.getCartItems().add(cartItem2);

        when(interactionRepository.existsByActiveIngredients(ingredient1, ingredient2)).thenReturn(false);

        boolean result = orderService.checkForInteractions(cart);
        assertFalse(result);
    }
}

