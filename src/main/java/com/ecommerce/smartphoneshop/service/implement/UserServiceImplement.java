package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.PasswordResetToken;
import com.ecommerce.smartphoneshop.domain.User;
import com.ecommerce.smartphoneshop.dto.UserDTO;
import com.ecommerce.smartphoneshop.repository.TokenRepository;
import com.ecommerce.smartphoneshop.repository.UserRepository;
import com.ecommerce.smartphoneshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final TokenRepository tokenRepository;

    @Autowired
    public UserServiceImplement(UserRepository userRepository, JavaMailSender mailSender, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public User save(UserDTO userDTO) {
        User user = User.builder()
                .name(userDTO.getName())
                .phone(userDTO.getPhone())
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .check_user(2)
                .date_signup(LocalDateTime.now())
                .build();
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public User updateInfo(User user, String phone, String email, String address) {
        user.setPhone(phone);
        user.setEmail(email);
        user.setAddress(address);

        return userRepository.save(user);
    }

    @Override
    public int getNewUsersThisMonth() {
        return userRepository.getNewUsersThisMonth();
    }

    @Override
    public User changePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }

    @Override
    public String sendEmail(User user) {
        try {
            String resetLink = generateResetToken(user);
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("duc.nh03dev@gmail.com");
            msg.setTo(user.getEmail());

            msg.setSubject("NHD - Đặt lại mật khẩu");
            msg.setText("Xin chào, bạn vừa yêu cầu cập nhật lại mật khẩu tại NHD. Đây là liên kết để bạn cập nhật mật khẩu: " + resetLink + ".\n\n");

            mailSender.send(msg);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private String generateResetToken(User user) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime expiry = current.plusMinutes(30);
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setUser(user);
        resetToken.setToken(uuid.toString());
        resetToken.setExpiryDateTime(expiry);
        PasswordResetToken token = tokenRepository.save(resetToken);
        if (token != null) {
            String endpointUrl = "http://localhost:2004/resetPassword";
            return endpointUrl + "/" + resetToken.getToken();
        }
        return "";
    }

    public boolean hasExpired(LocalDateTime expiryDateTime) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return expiryDateTime.isAfter(currentDateTime);
    }
}
