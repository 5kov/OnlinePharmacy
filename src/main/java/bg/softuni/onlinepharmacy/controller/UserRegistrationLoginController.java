package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.dto.LoginDTO;
import bg.softuni.onlinepharmacy.model.dto.RegisterDTO;
import bg.softuni.onlinepharmacy.model.dto.UpdateUserDTO;
import bg.softuni.onlinepharmacy.model.entity.UserEntity;
import bg.softuni.onlinepharmacy.repository.UserRepository;
import bg.softuni.onlinepharmacy.service.impl.ManageUserServiceImpl;
import bg.softuni.onlinepharmacy.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class UserRegistrationLoginController {

    private final UserServiceImpl userServiceImpl;
    private final ManageUserServiceImpl manageUserServiceImpl;
    private final UserRepository userRepository;

    public UserRegistrationLoginController(UserServiceImpl userServiceImpl, ManageUserServiceImpl manageUserServiceImpl, UserRepository userRepository) {
        this.userServiceImpl = userServiceImpl;
        this.manageUserServiceImpl = manageUserServiceImpl;
        this.userRepository = userRepository;
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
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerData", data);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            return "redirect:/register";
        }
        String success = userServiceImpl.register(data);
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

    @GetMapping("/user-settings")
    public String showSettingsForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UpdateUserDTO updateUserDTO = manageUserServiceImpl.findByUsername(currentPrincipalName);

//        UpdateUserDTO updateUserDTO = manageUserService.findById(userId);
        model.addAttribute("updateUserDTO", updateUserDTO);
        return "user-settings";
    }

    @PostMapping("/user-settings")
    public String updateSettings(@ModelAttribute("updateUserDTO") @Valid UpdateUserDTO updateUserDTO,
                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("updateUserDTO", updateUserDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            return "user-settings";
        }
        String success = manageUserServiceImpl.updateUser(updateUserDTO);
        if (success.equals("usernameExists")) {
            redirectAttributes.addFlashAttribute("updateUserDTO", updateUserDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            redirectAttributes.addFlashAttribute("usernameExists", true);
            return "redirect:/user-settings";
        } else if (success.equals("emailExists")) {
            redirectAttributes.addFlashAttribute("updateUserDTO", updateUserDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            redirectAttributes.addFlashAttribute("emailExists", true);
            return "redirect:/user-settings";
        } else if (success.equals("passwordsDoNotMatch")) {
            redirectAttributes.addFlashAttribute("updateUserDTO", updateUserDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            redirectAttributes.addFlashAttribute("passwordsDoNotMatch", true);
            return "redirect:/user-settings";
        }
        return "redirect:/success";
    }

    @GetMapping("/success")
    public String showSuccessForm() {
        return "success";
    }

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserEntity userEntity = userRepository.findByUsername(currentPrincipalName).get();
        UpdateUserDTO updateUserDTO = manageUserServiceImpl.findById(userEntity.getId());
        model.addAttribute("updateUserDTO", updateUserDTO);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String updateChangePassword(@ModelAttribute("updateUserDTO") @Valid UpdateUserDTO updateUserDTO,
                                 BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute("updateUserDTO", updateUserDTO);
//            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
//            return "change-password";
//        }
        String success = manageUserServiceImpl.updatePassword(updateUserDTO);
        if (success.equals("passwordsDoNotMatch")) {
            redirectAttributes.addFlashAttribute("updateUserDTO", updateUserDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            redirectAttributes.addFlashAttribute("passwordsDoNotMatch", true);
            return "redirect:/change-password";
        }
        if (success.equals("passwordsLength")) {
            redirectAttributes.addFlashAttribute("updateUserDTO", updateUserDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerData", bindingResult);
            redirectAttributes.addFlashAttribute("passwordLength", true);
            return "redirect:/change-password";
        }
        return "redirect:/user-settings";
    }

}
