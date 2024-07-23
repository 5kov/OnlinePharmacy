package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.entity.*;
import bg.softuni.onlinepharmacy.repository.*;
import jakarta.persistence.PreRemove;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSession userSession;
    @Autowired
    private InteractionRepository interactionRepository;

    @Transactional
    public void addToCart(User user, Long medicineId, int quantity) {

        Cart cart = user.getCart();
        Medicine medicine = medicineRepository.findById(medicineId).orElseThrow();

        Optional<CartItem> result = cartItemRepository.findCartItemByCartIdAndMedicineId(cart.getId(), medicineId);
        if (!result.isPresent()) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setMedicine(medicine);
            cartItem.setQuantity(quantity);
            cartItem.setItemPrice(medicine.getPrice() * quantity);
            cart.getCartItems().add(cartItem);
            cartRepository.save(cart);
        } else {
            //            // Medicine already in the cart, update the quantity
            CartItem cartItem = result.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        }
    }

    // Method to get current cart, update items, delete items, calculate total price, etc.

    @Transactional
    public void updateCartItem(Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    @Transactional
    @PreRemove
    public void deleteCartItem(Long itemId) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        Cart cart = item.getCart();
        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);
    }


    @Transactional
    public void createOrderFromCart() {
        User user = userRepository.findById(userSession.getId()).get();
        Cart cart = user.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setDrug(cartItem.getMedicine());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setItemPrice(cartItem.getItemPrice());
            order.getOrderItems().add(orderItem);
            orderItemRepository.save(orderItem);
        }

        orderRepository.save(order);
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Transactional
    public boolean placeOrder() {
        User user = userRepository.findById(userSession.getId()).get();
        Cart cart = user.getCart();
        if (cart.getCartItems().isEmpty()) {
            // If the cart is empty, throw an exception or simply return false
            throw new IllegalStateException("Cannot place an order because the cart is empty");
        }
        if (checkForInteractions(cart)) {
            throw new IllegalStateException("Cannot place order due to incompatible ingredients");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setDrug(cartItem.getMedicine());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setItemPrice(cartItem.getItemPrice());
            order.getOrderItems().add(orderItem);
            orderItemRepository.save(orderItem);
        }

        orderRepository.save(order);
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return true;
    }

    private boolean checkForInteractions(Cart cart) {
        Set<ActiveIngredient> ingredients = new HashSet<>();
        for (CartItem item : cart.getCartItems()) {
            ingredients.add(item.getMedicine().getActiveIngredient());
        }

        for (ActiveIngredient ingredientOne : ingredients) {
            for (ActiveIngredient ingredientTwo : ingredients) {
                if (!ingredientOne.equals(ingredientTwo) && interactionRepository.existsByActiveIngredients(ingredientOne, ingredientTwo)) {
                    return true; // Interaction found
                }
            }
        }
        return false; // No interactions found
    }

    public Cart getCurrentCart() {
        User user = userRepository.findById(userSession.getId()).get();
        return cartRepository.findById(user.getCart().getId()).orElseThrow();
    }

    public double calculateTotalPrice(Cart cart) {
        if (cart == null || cart.getCartItems() == null) {
            return 0.0;
        }
        return cart.getCartItems().stream()
                .mapToDouble(item -> item.getItemPrice() * item.getQuantity())
                .sum();
    }
}
