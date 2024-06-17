package com.ecommerce.smartphoneshop.CUSTOMER;

import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import com.ecommerce.smartphoneshop.domain.User;
import com.ecommerce.smartphoneshop.dto.UserDTO;
import com.ecommerce.smartphoneshop.repository.CartRepository;
import com.ecommerce.smartphoneshop.service.implement.UserServiceImplement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Log
public class AuthController {
    private final UserServiceImplement userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CartRepository cartRepository;

    @GetMapping("/register")
    public String register(Model model) {
        UserDTO userDTO = UserDTO.builder().build();
        model.addAttribute("userDTO", userDTO);
        return "user-register";
    }

    @PostMapping("register-new")
    public String addNewUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                             BindingResult result,
                             Model model) {
        if (result.hasErrors()){
            model.addAttribute("userDTO", userDTO);
            return "user-register";
        }

        User user1 = userService.findByUsername(userDTO.getUsername());
        User user2 = userService.findByEmail(userDTO.getEmail());
        User user3 = userService.findByPhone(userDTO.getPhone());

        if (user1 != null){
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("usernameError", "Tên đăng nhập đã tồn tại");
            return "user-register";
        }

        if (user2 != null){
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("emailError", "Email đã tồn tại");
            return "user-register";
        }

        if (user3 != null){
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("phoneError", "Số điện thoại đã tồn tại");
            return "user-register";
        }

        if (userDTO.getPassword().equals(userDTO.getRepeatPassword())){
            userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            User user = userService.save(userDTO);
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            cartRepository.save(shoppingCart);
            model.addAttribute("success", "Đăng ký thành công!");
        } else {
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("passwordError", "Mật khẩu không khớp!");
        }

        return "user-register";
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            UserDTO userDTO = UserDTO.builder().build();
            model.addAttribute("userDTO", userDTO);
            return "user-login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Bạn đã đăng nhập vào hệ thống!");
            return "redirect:/home";
        }
    }

    @GetMapping("/change-password")
    public String changePassword(Principal principal) {
        if (principal == null) {
            return "user-login";
        } else {
            return "user-change-password";
        }
    }

    @PostMapping("/update-password")
    public String updatePassword(Model model, Principal principal,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword) {
        if (principal == null) {
            return "user-login";
        } else {
            User user = userService.findByUsername(principal.getName());
            if (newPassword.equals(confirmPassword)) {
                if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                    userService.changePassword(user, bCryptPasswordEncoder.encode(newPassword));
                    model.addAttribute("success", "Cập nhật mật khẩu thành công!");
                } else {
                    model.addAttribute("error", "Mật khẩu cũ không đúng!");
                }
            } else {
                model.addAttribute("error", "Mật khẩu mới không khớp!");
            }
        }
        return "redirect:/info";
    }
}