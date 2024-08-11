package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.entity.Cart;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.impl.CartServiceImpl;
import bg.softuni.onlinepharmacy.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class CartController {
    @Autowired
    private CartServiceImpl cartServiceImpl;
    @Autowired
    private OrderServiceImpl orderServiceImpl;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/order-finished")
    public String viewOrderFinished(){
        return "order-finished";
    }

    @PostMapping("/add")
    public ModelAndView addToCart(@RequestParam Long medicineId, @RequestParam int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserEntity userEntity = userRepository.findByUsername(currentPrincipalName).get();
        cartServiceImpl.addToCart(userEntity, medicineId, quantity);
        return new ModelAndView("redirect:/medicines");
    }

    @GetMapping("/cart")
    public String showCart(Model model) {
        Cart cart = cartServiceImpl.getCurrentCart();
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cartServiceImpl.calculateTotalPrice(cart));
        return "cart";
    }

    @PostMapping("/update-order/{itemId}")
    public String updateItem(@PathVariable Long itemId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        cartServiceImpl.updateCartItem(itemId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Cart updated successfully!");
        return "redirect:/cart";
    }

    @PostMapping("/delete-order/{itemId}")
    public String deleteItem(@PathVariable Long itemId, RedirectAttributes redirectAttributes) {
        cartServiceImpl.deleteCartItem(itemId);
        redirectAttributes.addFlashAttribute("successMessage", "Item removed successfully!");
        return "redirect:/cart";
    }

    @PostMapping("/order")
    public String placeOrder(RedirectAttributes redirectAttributes) {
        try {
            if (orderServiceImpl.placeOrder()) {
                redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully!");
            }
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/cart"; // Redirect back to cart if there is an interaction
        }
        return "redirect:/order-finished";
    }
}
