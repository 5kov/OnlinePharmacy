package bg.softuni.onlinepharmacy.controller;


import bg.softuni.onlinepharmacy.model.dto.ActiveIngredientDTO;
import bg.softuni.onlinepharmacy.model.dto.MedicineDTO;
import bg.softuni.onlinepharmacy.model.entity.*;
import bg.softuni.onlinepharmacy.repository.ActiveIngredientRepository;
import bg.softuni.onlinepharmacy.repository.InteractionRepository;
import bg.softuni.onlinepharmacy.repository.MedicineRepository;
import bg.softuni.onlinepharmacy.service.ManageUserService;
import bg.softuni.onlinepharmacy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class AdministratorController {
    private final UserService userService;
    private final ManageUserService manageUserService;
    @Autowired
    private ActiveIngredientRepository ingredientRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private ActiveIngredientRepository activeIngredientRepository;
    @Autowired
    private InteractionRepository interactionRepository;

    public AdministratorController(UserService userService, ManageUserService manageUserService) {
        this.userService = userService;
        this.manageUserService = manageUserService;
    }

    @GetMapping("/administrator-manage-users")
    public String showUserManagement(Model model, @RequestParam(required = false) String search) {
        List<UserEntity> users = manageUserService.searchUsers(search == null ? "" : search);
        model.addAttribute("users", users);
        return "administrator-manage-users";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        boolean success = manageUserService.deleteUser(userId);
        if (!success) {
            redirectAttributes.addFlashAttribute("error", "Cannot remove the last admin.");
        }
        return "redirect:/administrator-manage-users";
    }

    @PostMapping("/toggle-role")
    public String toggleRole(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        boolean success = manageUserService.toggleUserRole(userId);
        if (!success) {
            redirectAttributes.addFlashAttribute("error", "Cannot remove the last admin.");
        }
        return "redirect:/administrator-manage-users";
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
        Medicine medicine = medicineRepository.findById(medicineDTO.getId())
                .orElse(new Medicine()); // Only create a new instance if the ID does not exist
        medicine.setMedicineNameEn(medicineDTO.getMedicineNameEn());
        medicine.setMedicineNameBg(medicineDTO.getMedicineNameBg());
        medicine.setPrice(medicineDTO.getPrice());
        medicine.setDescriptionEn(medicineDTO.getDescriptionEn());
        medicine.setDescriptionBg(medicineDTO.getDescriptionBg());
        medicine.setImageUrl(medicineDTO.getImageUrl());
        if (medicineDTO.getActiveIngredientId() != null) {
            ActiveIngredient ingredient = ingredientRepository.findById(medicineDTO.getActiveIngredientId())
                    .orElse(null);
            medicine.setActiveIngredient(ingredient);
        }
        return medicine;
    }

    @GetMapping("/administrator-manage-medicines")
    public String listMedicines(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("medicines", medicineRepository.findByMedicineNameEnContainingIgnoreCase(search));
        } else {
            model.addAttribute("medicines", medicineRepository.findAll());
        }
        model.addAttribute("ingredients", ingredientRepository.findAll());
        return "administrator-manage-medicines";
    }

    @PostMapping("/save")
    public String saveMedicine(@ModelAttribute("medicineDTO") @Valid MedicineDTO medicineDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("medicines", medicineRepository.findAll());
            model.addAttribute("ingredients", ingredientRepository.findAll());
            return "administrator-manage-medicines";
        }
        Medicine medicine = convertToEntity(medicineDTO);
        medicineRepository.save(medicine);
        return "redirect:/administrator-manage-medicines";
    }

    @PostMapping("/delete/{id}")
    public String deleteMedicine(@PathVariable Long id) {
        medicineRepository.deleteById(id);
        return "redirect:/administrator-manage-medicines";
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<MedicineDTO> getMedicineData(@PathVariable Long id) {
        Optional<Medicine> medicine = medicineRepository.findById(id);
        if (medicine.isPresent()) {
            MedicineDTO dto = convertToDto(medicine.get());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    private MedicineDTO convertToDto(Medicine medicine) {
        MedicineDTO dto = new MedicineDTO();
        dto.setId(medicine.getId());
        dto.setMedicineNameEn(medicine.getMedicineNameEn());
        dto.setMedicineNameBg(medicine.getMedicineNameBg());
        dto.setPrice(medicine.getPrice());
        dto.setDescriptionEn(medicine.getDescriptionEn());
        dto.setDescriptionBg(medicine.getDescriptionBg());
        dto.setImageUrl(medicine.getImageUrl());
        dto.setActiveIngredientId(medicine.getActiveIngredient().getId());
        return dto;
    }

    @GetMapping("/administrator-add-interactions")
    public String showAddInteractionForm(Model model) {
        model.addAttribute("ingredients", activeIngredientRepository.findAll());
        model.addAttribute("interactionTypes", InteractionTypeEnum.values());
        return "administrator-add-interactions";
    }

    @PostMapping("/administrator-add-interactions")
    public String saveInteraction(
            @RequestParam Long drugNameId,
            @RequestParam Long interactionDrugId,
            @RequestParam InteractionTypeEnum interactionType,
            Model model) {
        ActiveIngredient drugName = activeIngredientRepository.findById(drugNameId).orElse(null);
        ActiveIngredient interactionDrug = activeIngredientRepository.findById(interactionDrugId).orElse(null);

        if (drugName == null || interactionDrug == null) {
            model.addAttribute("errorMessage", "Invalid active ingredient selection.");
        } else {
            if (interactionRepository.findByDrugNameAndInteractionDrugAndInteractionType(drugName, interactionDrug, interactionType).isPresent()) {
                model.addAttribute("errorMessage", "This interaction combination already exists.");
            } else {
                Interaction interaction = new Interaction();
                interaction.setDrugName(drugName);
                interaction.setInteractionDrug(interactionDrug);
                interaction.setInteractionType(interactionType);
                interactionRepository.save(interaction);
                return "redirect:/administrator-add-interactions";
            }
        }

        model.addAttribute("ingredients", activeIngredientRepository.findAll());
        model.addAttribute("interactionTypes", InteractionTypeEnum.values());
        return "administrator-add-interactions";
    }

    @GetMapping("/administrator-manage-interactions")
    public String listInteractions(Model model) {
        model.addAttribute("interactions", interactionRepository.findAll());
        return "administrator-manage-interactions";
    }

    @PostMapping("/delete-interaction/{id}")
    public String deleteInteraction(@PathVariable Long id) {
        interactionRepository.deleteById(id);
        return "redirect:/administrator-manage-interactions";
    }


}
