package bg.softuni.onlinepharmacy.service.impl;


import bg.softuni.onlinepharmacy.model.entity.Cart;
import bg.softuni.onlinepharmacy.model.entity.CartItem;
import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.*;
import bg.softuni.onlinepharmacy.service.CartService;
import jakarta.persistence.PreRemove;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
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

    @Transactional
    @Override
    public void addToCart(UserEntity userEntity, Long medicineId, int quantity) {

        Cart cart = userEntity.getCart();
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
            CartItem cartItem = result.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        }
    }


    @Transactional
    @Override
    public void updateCartItem(Long itemId, int quantity) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        item.setQuantity(quantity);
        cartItemRepository.save(item);
    }

    @Transactional
    @PreRemove
    @Override
    public void deleteCartItem(Long itemId) {
        CartItem item = cartItemRepository.findById(itemId).orElseThrow();
        Cart cart = item.getCart();
        cart.getCartItems().remove(item);
        cartItemRepository.delete(item);
    }
    @Override
    public Cart getCurrentCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserEntity userEntity = userRepository.findByUsername(currentPrincipalName).get();
        return cartRepository.findById(userEntity.getCart().getId()).orElseThrow();
    }
    @Override
    public double calculateTotalPrice(Cart cart) {
        if (cart == null || cart.getCartItems() == null) {
            return 0.0;
        }
        return cart.getCartItems().stream()
                .mapToDouble(item -> item.getItemPrice() * item.getQuantity())
                .sum();
    }
}
