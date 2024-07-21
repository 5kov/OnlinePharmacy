package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.config.UserSession;
import bg.softuni.onlinepharmacy.model.dto.LoginDTO;
import bg.softuni.onlinepharmacy.model.dto.RegisterDTO;
import bg.softuni.onlinepharmacy.model.entity.User;
import bg.softuni.onlinepharmacy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



@Controller
public class UserRegistrationLoginController {

    private final UserService userService;
    private final UserSession userSession;

    public UserRegistrationLoginController(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;
    }

    @ModelAttribute("registerData")
    public RegisterDTO registerDTO() {
        return new RegisterDTO();
    }

    @ModelAttribute("loginData")
    public LoginDTO loginDTO() {
        return new LoginDTO();
    }

    @GetMapping("/login")
    public String viewLogin(){
        return "login";
    }
    @GetMapping("/register")
    public String viewRegister(){
        return "register";
    }

    @GetMapping("/index")
    public String viewIndex(){
        return "index";
    }

    @PostMapping("/register")
    public String doRegister(@Valid RegisterDTO data, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        System.out.println();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerData", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            return "redirect:/register";
        }
        String success = userService.register(data);
        if (success.equals("usernameExists")) {
            redirectAttributes.addFlashAttribute("registerData", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            redirectAttributes.addFlashAttribute("usernameExists", true);
            return "redirect:/register";
        } else if (success.equals("emailExists")) {
            redirectAttributes.addFlashAttribute("registerData", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            redirectAttributes.addFlashAttribute("emailExists", true);
            return "redirect:/register";
        } else if (success.equals("passwordsDoNotMatch")) {
            redirectAttributes.addFlashAttribute("registerData", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            redirectAttributes.addFlashAttribute("passwordsDoNotMatch", true);
            return "redirect:/register";
        }

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String doLogin(@Valid LoginDTO data, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if(userSession.isLoggedIn()) {
            return "redirect:/home";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("loginData", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginData", bindingResult);
            return "redirect:/login";
        }

        boolean success = userService.login(data);
        if (!success) {
            redirectAttributes.addFlashAttribute("loginData", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginData", bindingResult);
            redirectAttributes.addFlashAttribute("wrongUsernameOrPassword", true);
            return "redirect:/login";
        }

        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String doLogout() {
        userService.logout();
        return "redirect:/index";
    }

}
