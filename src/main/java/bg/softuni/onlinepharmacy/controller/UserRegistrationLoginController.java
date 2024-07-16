package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.dto.LoginDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserRegistrationLoginController {

    @GetMapping("/login")
    public String viewLogin(){
        return "login";
    }
    @GetMapping("/register")
    public String viewRegister(){
        return "register";
    }

}
