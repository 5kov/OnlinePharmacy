package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.dto.ActiveIngredientDTO;
import bg.softuni.onlinepharmacy.model.dto.MedicineDTO;
import bg.softuni.onlinepharmacy.model.entity.ActiveIngredient;
import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.model.entity.User;
import bg.softuni.onlinepharmacy.repository.ActiveIngredientRepository;
import bg.softuni.onlinepharmacy.repository.MedicineRepository;
import bg.softuni.onlinepharmacy.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AdministratorController {
    private final UserService userService;
    @Autowired
    private ActiveIngredientRepository ingredientRepository;
    @Autowired
    private MedicineRepository medicineRepository;


    public AdministratorController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/administrator-add-interactions")
    public String viewAddInteraction(){
        return "administrator-add-interactions";
    }
    @GetMapping("/administrator-manage-users")
    public String viewManageUsers(){
        return "administrator-manage-users";
    }
    @GetMapping("/administrator-manage-medicines")
    public String viewManageMedicines(){
        return "administrator-manage-medicines";
    }
    @GetMapping("/administrator-manage-interactions")
    public String viewManageInteractions(){
        return "administrator-manage-interactions";
    }

    @PostMapping("/administrator-manage-users")
    public String searchUsers(@RequestParam("username") String username, Model model) {
        List<User> users = userService.findUsersByUsername(username);
        model.addAttribute("users", users);
        model.addAttribute("username", username);
        return "administrator-manage-users";
    }
    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id, @RequestParam("username") String username, Model model) {
        try {
            userService.deleteUser(id);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return searchUsers(username, model); // Re-populate the search page with the current results
        }
        return "redirect:/administrator-manage-users?username=" + username;
    }

    @PostMapping("/toggleAdminStatus/{id}")
    public String toggleAdminStatus(@PathVariable Long id, @RequestParam("username") String username, @RequestParam boolean adminStatus, Model model) {
        try {
            userService.toggleAdminStatus(id, adminStatus);
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            return searchUsers(username, model); // Re-populate the search page with the current results
        }

        return "redirect:/administrator-manage-users?username=" + username;
    }






    @GetMapping("/administrator-add-active-ingredient")
    public String showAddIngredientForm(Model model) {
        model.addAttribute("ingredient", new ActiveIngredientDTO()); // Use DTO
        return "administrator-add-active-ingredient";
    }

    @PostMapping("/administrator-add-active-ingredient")
    public String addIngredient(@ModelAttribute("ingredient") @Valid ActiveIngredientDTO ingredientDTO, BindingResult result, Model model) {
        if (ingredientRepository.existsByIngredientName(ingredientDTO.getIngredientName())) {
            result.rejectValue("ingredientName", "ingredientName.exist", "Active ingredient with this name already exists");
        }
        if (ingredientRepository.existsByIngredientCode(ingredientDTO.getIngredientCode())) {
            result.rejectValue("ingredientCode", "ingredientCode.exist", "Active ingredient with this code already exists");
        }

        if (result.hasErrors()) {
            return "administrator-add-active-ingredient";
        }

        // Conversion to entity and save logic goes here if validation passes
        ActiveIngredient ingredient = new ActiveIngredient();
        ingredient.setIngredientName(ingredientDTO.getIngredientName());
        ingredient.setIngredientCode(ingredientDTO.getIngredientCode());
        ingredientRepository.save(ingredient);
        return "redirect:/administrator-add-active-ingredient";
    }

    @GetMapping("/administrator-manage-active-ingredients")
    public String showIngredients(Model model) {
        List<ActiveIngredient> ingredients = ingredientRepository.findAll();
        model.addAttribute("ingredients", ingredients);
        return "administrator-manage-active-ingredients";
    }

    @PostMapping("/deleteIngredient/{id}")
    public String deleteIngredient(@PathVariable Long id) {
        ingredientRepository.deleteById(id);
        return "redirect:/administrator-manage-active-ingredients"; // Redirect back to the management page
    }

    @GetMapping("/administrator-add-medicines")
    public String showAddForm(Model model) {
        model.addAttribute("medicine", new MedicineDTO());
        model.addAttribute("ingredients", ingredientRepository.findAll());
        return "administrator-add-medicines";
    }

    @PostMapping("/administrator-add-medicines")
    public String addMedicine(@ModelAttribute("medicine") @Valid MedicineDTO medicineDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ingredients", ingredientRepository.findAll());
            return "administrator-add-medicines";
        }

        // Check for existing names
        if (medicineRepository.existsByMedicineNameEn(medicineDTO.getMedicineNameEn())) {
            result.rejectValue("medicineNameEn", "error.medicineNameEn", "There is already a medicine with this English name in the database.");
        }
        if (medicineRepository.existsByMedicineNameBg(medicineDTO.getMedicineNameBg())) {
            result.rejectValue("medicineNameBg", "error.medicineNameBg", "There is already a medicine with this Bulgarian name in the database.");
        }

        if (result.hasErrors()) {
            model.addAttribute("ingredients", ingredientRepository.findAll());
            return "administrator-add-medicines";
        }

        Medicine medicine = convertToEntity(medicineDTO);
        medicineRepository.save(medicine);
        return "redirect:/administrator-add-medicines";
    }

    private Medicine convertToEntity(MedicineDTO medicineDTO) {
        Medicine medicine = new Medicine();
        medicine.setMedicineNameEn(medicineDTO.getMedicineNameEn());
        medicine.setMedicineNameBg(medicineDTO.getMedicineNameBg());
        medicine.setPrice(medicineDTO.getPrice());
        medicine.setDescriptionEn(medicineDTO.getDescriptionEn());
        medicine.setDescriptionBg(medicineDTO.getDescriptionBg());
        medicine.setImageUrl(medicineDTO.getImageUrl());
        if (medicineDTO.getActiveIngredientId() != null) {
            Optional<ActiveIngredient> ingredient = ingredientRepository.findById(medicineDTO.getActiveIngredientId());
            ingredient.ifPresent(medicine::setActiveIngredient);
        }
        return medicine;
    }



}
