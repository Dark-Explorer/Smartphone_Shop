package com.ecommerce.smartphoneshop.CUSTOMER;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {
    @GetMapping("/login")
    public String showLogin() {
        return "user-login";
    }

    @GetMapping("/home")
    public String showHome() {
        return "user-home";
    }

    @GetMapping("/register")
    public String showRegister() {
        return "user-register";
    }

    @GetMapping("/product")
    public String showProduct() {
        return "admin-products";
    }

}
