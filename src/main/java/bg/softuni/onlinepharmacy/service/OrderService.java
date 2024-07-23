package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.entity.*;
import bg.softuni.onlinepharmacy.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSession userSession;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private InteractionRepository interactionRepository;

    @Transactional
    public boolean placeOrder() {
        User user = userRepository.findById(userSession.getId()).get();
        Cart cart = user.getCart();
        if (cart.getCartItems().isEmpty()) {
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
                    return true;
                }
            }
        }
        return false;
    }
}
