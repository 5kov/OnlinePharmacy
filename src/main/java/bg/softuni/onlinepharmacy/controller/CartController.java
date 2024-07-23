package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.entity.Cart;
import bg.softuni.onlinepharmacy.model.entity.User;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CartController {

    private UserRepository userRepository;
    @Autowired
    private UserSession userSession;


    @GetMapping("/order-finished")
    public String viewOrderFinished(){
        return "order-finished";
    }



    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ModelAndView addToCart(@RequestParam Long medicineId, @RequestParam int quantity) {
        User user = userRepository.findById(userSession.getId()).get(); // Assume you have a method to get the currently logged-in user
        cartService.addToCart(user, medicineId, quantity);
        return new ModelAndView("redirect:/medicines");
    }





    @GetMapping("/cart")
    public String showCart(Model model) {
        Cart cart = cartService.getCurrentCart();
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart));
        return "cart";
    }

    @PostMapping("/update-order/{itemId}")
    public String updateItem(@PathVariable Long itemId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        cartService.updateCartItem(itemId, quantity);
        redirectAttributes.addFlashAttribute("successMessage", "Cart updated successfully!");
        return "redirect:/cart";
    }

    @PostMapping("/delete-order/{itemId}")
    public String deleteItem(@PathVariable Long itemId, RedirectAttributes redirectAttributes) {
        cartService.deleteCartItem(itemId);
        redirectAttributes.addFlashAttribute("successMessage", "Item removed successfully!");
        return "redirect:/cart";
    }

    @PostMapping("/order")
    public String placeOrder(RedirectAttributes redirectAttributes) {
        cartService.createOrderFromCart();
        redirectAttributes.addFlashAttribute("successMessage", "Order placed successfully!");
        return "redirect:/cart";
    }

}