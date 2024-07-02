package com.ecommerce.smartphoneshop.CUSTOMER;

import com.ecommerce.smartphoneshop.domain.PasswordResetToken;
import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import com.ecommerce.smartphoneshop.domain.User;
import com.ecommerce.smartphoneshop.dto.UserDTO;
import com.ecommerce.smartphoneshop.repository.CartRepository;
import com.ecommerce.smartphoneshop.repository.TokenRepository;
import com.ecommerce.smartphoneshop.service.implement.UserServiceImplement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
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
    private final TokenRepository tokenRepository;

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

    @PostMapping("/update-password")
    public String updatePassword(Principal principal,
                                 @RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "user-login";
        } else {
            User user = userService.findByUsername(principal.getName());
            if (newPassword.equals(confirmPassword)) {
                if (bCryptPasswordEncoder.matches(oldPassword, user.getPassword())) {
                    userService.changePassword(user, bCryptPasswordEncoder.encode(newPassword));
                    redirectAttributes.addFlashAttribute("success", "Cập nhật mật khẩu thành công!");
                } else {
                    redirectAttributes.addFlashAttribute("error", "Mật khẩu cũ không đúng!");
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu mới không khớp!");
            }
        }
        return "redirect:/info";
    }

    @GetMapping("forgot-password")
    public String forgotPassword() {
        return "user-forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPasswordProcess(@RequestParam("email") String email) {
        User user = userService.findByEmail(email);
        String output = "";
        if (user != null) {
            output = userService.sendEmail(user);
        }
        if (output.equals("success")) {
            return "redirect:/forgot-password?success";
        } else return "redirect:/login?error";
    }

    @GetMapping("/resetPassword/{token}")
    public String resetPassword(Model model, @PathVariable String token) {
        PasswordResetToken reset = tokenRepository.findByToken(token);
        if (reset != null && userService.hasExpired(reset.getExpiryDateTime())) {
            model.addAttribute("email", reset.getUser().getEmail());
            return "user-reset-password";
        }
        return "redirect:/forgot-password?error";
    }

    @PostMapping("/resetPassword")
    public String passwordResetProcess(@ModelAttribute("email") String email,
                                       @RequestParam("newPassword") String newPassword,
                                       RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(email);
        if (user != null) {
            userService.changePassword(user, bCryptPasswordEncoder.encode(newPassword));
            redirectAttributes.addFlashAttribute("success", "Mật khẩu đã được cập nhật! Bạn có thể đăng nhập lại!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Email không khớp!");
        }
        return "redirect:/login";
    }
}