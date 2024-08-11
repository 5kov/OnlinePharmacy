package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.entity.Cart;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.impl.CartServiceImpl;
import bg.softuni.onlinepharmacy.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartServiceImpl cartServiceImpl;

    @MockBean
    private OrderServiceImpl orderServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testViewOrderFinished() throws Exception {
        mockMvc.perform(get("/order-finished"))
                .andExpect(status().isOk())
                .andExpect(view().name("order-finished"));
    }

    @Test
    public void testAddToCart() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userEntity));

        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("medicineId", "1")
                        .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medicines"));
    }

    @Test
    public void testShowCart() throws Exception {
        Cart cart = new Cart();
        cart.setCartItems(Collections.emptyList()); // Ensure cartItems is not null
        when(cartServiceImpl.getCurrentCart()).thenReturn(cart);
        when(cartServiceImpl.calculateTotalPrice(cart)).thenReturn(100.0);

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("cartItems"))
                .andExpect(model().attributeExists("totalPrice"));
    }

    @Test
    public void testUpdateItem() throws Exception {
        mockMvc.perform(post("/update-order/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("quantity", "3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    public void testDeleteItem() throws Exception {
        mockMvc.perform(post("/delete-order/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    public void testPlaceOrder() throws Exception {
        when(orderServiceImpl.placeOrder()).thenReturn(true);

        mockMvc.perform(post("/order"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/order-finished"))
                .andExpect(flash().attributeExists("successMessage"));
    }

    @Test
    public void testPlaceOrderWithException() throws Exception {
        when(orderServiceImpl.placeOrder()).thenThrow(new IllegalStateException("Test Exception"));

        mockMvc.perform(post("/order"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"))
                .andExpect(flash().attributeExists("errorMessage"));
    }
}
