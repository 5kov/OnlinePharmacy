package bg.softuni.onlinepharmacy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministratorController {
    @GetMapping("/administrator-add-medicines")
    public String viewAddMedicine(){
        return "administrator-add-medicines";
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
}
