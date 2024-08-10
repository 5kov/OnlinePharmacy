package bg.softuni.onlinepharmacy.service.impl;

import bg.softuni.onlinepharmacy.model.entity.Cart;
import bg.softuni.onlinepharmacy.model.entity.CartItem;
import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private UserEntity userEntity;
    private Cart cart;
    private Medicine medicine;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userEntity = new UserEntity();
        cart = new Cart();
        userEntity.setCart(cart);
        medicine = new Medicine();
        medicine.setPrice(10.0);

        cart.setId(1L);
        cart.setCartItems(new ArrayList<>());
    }

    @Test
    void addToCart_NewItem() {
        when(medicineRepository.findById(anyLong())).thenReturn(Optional.of(medicine));
        when(cartItemRepository.findCartItemByCartIdAndMedicineId(anyLong(), anyLong())).thenReturn(Optional.empty());

        cartService.addToCart(userEntity, 1L, 2);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(20.0, cart.getCartItems().get(0).getItemPrice());
        verify(cartRepository).save(cart);
    }

    @Test
    void addToCart_ExistingItem() {

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(2);
        cartItem.setItemPrice(20.0);
        cartItem.setMedicine(medicine);
        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);

        when(medicineRepository.findById(anyLong())).thenReturn(Optional.of(medicine));
        when(cartItemRepository.findCartItemByCartIdAndMedicineId(anyLong(), anyLong())).thenReturn(Optional.of(cartItem));

        cartService.addToCart(userEntity, 1L, 2);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(4, cart.getCartItems().get(0).getQuantity());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void updateCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);

        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));

        cartService.updateCartItem(1L, 5);

        assertEquals(5, cartItem.getQuantity());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void deleteCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cart.getCartItems().add(cartItem);

        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));

        cartService.deleteCartItem(1L);

        assertTrue(cart.getCartItems().isEmpty());
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void getCurrentCart() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(userEntity));
        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

        Cart result = cartService.getCurrentCart();

        assertEquals(cart, result);
    }

    @Test
    void calculateTotalPrice() {
        CartItem item1 = new CartItem();
        item1.setQuantity(2);
        item1.setItemPrice(10.0);

        CartItem item2 = new CartItem();
        item2.setQuantity(3);
        item2.setItemPrice(15.0);

        cart.getCartItems().add(item1);
        cart.getCartItems().add(item2);

        double totalPrice = cartService.calculateTotalPrice(cart);

        assertEquals(65.0, totalPrice);
    }

    @Test
    void calculateTotalPrice_EmptyCart() {
        double totalPrice = cartService.calculateTotalPrice(new Cart());
        assertEquals(0.0, totalPrice);
    }

    @Test
    void calculateTotalPrice_NullCart() {
        double totalPrice = cartService.calculateTotalPrice(null);
        assertEquals(0.0, totalPrice);
    }
}

