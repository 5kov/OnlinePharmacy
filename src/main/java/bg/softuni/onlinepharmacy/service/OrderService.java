package bg.softuni.onlinepharmacy.service;

import bg.softuni.onlinepharmacy.model.entity.*;

public interface OrderService {
    boolean placeOrder();

    boolean checkForInteractions(Cart cart);
}
