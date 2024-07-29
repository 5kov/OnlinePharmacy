package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.entity.Medicine;
import bg.softuni.onlinepharmacy.repository.MedicineRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class MedicineController {
    @Autowired
    private MedicineRepository medicineRepository;

    @GetMapping("/item")
    public String showMedicineDetails(@RequestParam Long medicineId, Model model, HttpServletRequest request) {
        Medicine medicine = medicineRepository.findById(medicineId).orElse(null);
        if (medicine == null) {
            model.addAttribute("error", "Medicine not found");
            return "errorPage"; // Redirect to a generic error page or back to list
        }
        model.addAttribute("medicine", medicine);
        model.addAttribute("request", request);
        return "item";
    }
}
