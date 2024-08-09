package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.CartRepository;
import bg.softuni.onlinepharmacy.repository.MedicineRepository;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.impl.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class PharmacyController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private CartServiceImpl cartServiceImpl;

    @GetMapping("/pharmacy")
    public String listMedicines(Model model,
                                @RequestParam(defaultValue = "") String search,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "9") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Medicine> medicinePage = medicineRepository.findBySearchTerm(search, pageable);

        model.addAttribute("medicines", medicinePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", medicinePage.getTotalPages());
        model.addAttribute("currentSize", size);
        model.addAttribute("search", search);
        return "pharmacy";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long medicineId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserEntity userEntity = userRepository.findByUsername(currentPrincipalName).get();
        cartServiceImpl.addToCart(userEntity, medicineId, quantity);
        redirectAttributes.addFlashAttribute("message", "Item added to cart successfully!");
        return "redirect:/pharmacy";
    }

}
