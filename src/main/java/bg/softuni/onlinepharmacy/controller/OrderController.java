package bg.softuni.onlinepharmacy.controller;

import bg.softuni.onlinepharmacy.model.entity.Order;
import bg.softuni.onlinepharmacy.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/orders")
    public String listOrders(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Order> orders = orderRepository.findAll(PageRequest.of(page, 1)); // Each page has 1 order
        model.addAttribute("orders", orders);
        return "administrator-view-orders";
    }
}