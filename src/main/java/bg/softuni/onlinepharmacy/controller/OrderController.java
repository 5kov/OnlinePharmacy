package bg.softuni.onlinepharmacy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {
    @GetMapping("/cart")
    public String viewCart(){
        return "cart";
    }
    @GetMapping("/order-finished")
    public String viewOrderFinished(){
        return "order-finished";
    }
}
