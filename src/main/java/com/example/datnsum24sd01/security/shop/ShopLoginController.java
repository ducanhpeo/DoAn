package com.example.datnsum24sd01.security.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ShopLoginController {
//    @GetMapping("/beestore/login")
//    public String getFormLoginTrangChu(){
//        return "customer-template/login";
//    }
//    @PostMapping("/besstore/login")
//    public String loginAdmin(@RequestParam String email, @RequestParam String password, RedirectAttributes redirectAttributes) {
//        // Dummy authentication logic for demonstration purposes
//        boolean isAuthenticated = authenticate(email, password);
//
//        if (!isAuthenticated) {
//            redirectAttributes.addAttribute("error", true);
//            return "redirect:besstore/login";
//        }
//        redirectAttributes.addAttribute("thanhcong", true);
//        // Successful login logic here
//        return "redirect:beestore/trangchu";
//    }
//
//    private boolean authenticate(String email, String password) {
//        // Replace with actual authentication logic (e.g., checking against a database)
//        return "admin@example.com".equals(email) && "password".equals(password);
//    }

    @GetMapping("/beestore/login")
    public String getFormLoginTrangChu(){
        return "customer-template/login";
    }

    @GetMapping("/beestore/dang-ky")
    public String getFormDangKyTrangChu(){
        return "customer-template/dangky";
    }
}
