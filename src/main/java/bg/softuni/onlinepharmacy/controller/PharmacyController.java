package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.model.entity.User;
import bg.softuni.onlinepharmacy.repository.MedicineRepository;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PharmacyController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSession userSession;

    @GetMapping("/item")
    public String viewItem(){
        return "item";
    }

    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private CartService cartService;

    @GetMapping("/pharmacy")
    public String listMedicines(Model model) {
        List<Medicine> allMedicines = medicineRepository.findAll();
        model.addAttribute("medicines", allMedicines);
        return "pharmacy";
    }




    @PostMapping("/add-to-cart")
    public ModelAndView addToCart(@RequestParam Long medicineId, @RequestParam int quantity) {
        User user = getCurrentUser(); // Assume you have a method to get the currently logged-in user
        cartService.addToCart(user, medicineId, quantity);
        return new ModelAndView("redirect:/pharmacy");
    }

    private User getCurrentUser() {
        return userRepository.findById(userSession.getId()).get();
    }

}
