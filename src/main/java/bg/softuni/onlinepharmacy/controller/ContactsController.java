package bg.softuni.onlinepharmacy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactsController {
    @GetMapping("/contacts")
    public String viewContacts(){
        return "contacts";
    }
    @GetMapping("/about")
    public String viewAbout(){
        return "about";
    }
}
