package bg.softuni.onlinepharmacy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PharmacyController {
    @GetMapping("/pharmacy")
    public String viewLogin(){
        return "pharmacy";
    }
    @GetMapping("/item")
    public String viewItem(){
        return "item";
    }
}
