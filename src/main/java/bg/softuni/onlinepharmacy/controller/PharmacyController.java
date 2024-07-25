package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.CartRepository;
import bg.softuni.onlinepharmacy.repository.MedicineRepository;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.CartService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class PharmacyController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserSession userSession;

    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private CartService cartService;

//    @GetMapping("/pharmacy")
//    public String listMedicines(Model model) {
//        List<Medicine> allMedicines = medicineRepository.findAll();
//        model.addAttribute("medicines", allMedicines);
//        return "pharmacy";
//    }

//    @GetMapping("/pharmacy")
//    public String listMedicines(Model model, @RequestParam(defaultValue = "0") int page) {
//        int pageSize = 9;
//        Pageable pageable = PageRequest.of(page, pageSize);
//        Page<Medicine> medicinePage = medicineRepository.findAll(pageable);
//
//        model.addAttribute("medicines", medicinePage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", medicinePage.getTotalPages());
//        return "pharmacy";
//    }

//    @GetMapping("/pharmacy")
//    public String listMedicines(Model model,
//                                @RequestParam(defaultValue = "") String search,
//                                @RequestParam(defaultValue = "0") int page,
//                                @RequestParam(defaultValue = "9") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        Page<Medicine> medicinePage;
//        if (search.isEmpty()) {
//            medicinePage = medicineRepository.findAll(pageable);
//        } else {
//            medicinePage = medicineRepository.findBySearchTerm(search, pageable);
//        }
//        model.addAttribute("medicines", medicinePage.getContent());
//        model.addAttribute("currentPage", page);
//        model.addAttribute("totalPages", medicinePage.getTotalPages());
//        model.addAttribute("search", search);
//        return "pharmacy";
//    }



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







//    @PostMapping("/add-to-cart")
//    public ModelAndView addToCart(@RequestParam Long medicineId, @RequestParam int quantity) {
//        UserEntity user = getCurrentUser();
//
//        cartService.addToCart(user, medicineId, quantity);
//        return new ModelAndView("redirect:/pharmacy");
//    }


    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam Long medicineId, @RequestParam int quantity, RedirectAttributes redirectAttributes) {
        UserEntity userEntity = getCurrentUser();
        cartService.addToCart(userEntity, medicineId, quantity);
        redirectAttributes.addFlashAttribute("message", "Item added to cart successfully!");
        return "redirect:/pharmacy";
    }




    private UserEntity getCurrentUser() {
        return userRepository.findById(userSession.getId()).get();
    }

}
