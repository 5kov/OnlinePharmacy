package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.model.entity.Cart;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;

public interface CartService {

    void addToCart(UserEntity userEntity, Long medicineId, int quantity);

    void updateCartItem(Long itemId, int quantity);

    void deleteCartItem(Long itemId);

    Cart getCurrentCart();

    double calculateTotalPrice(Cart cart);
}
