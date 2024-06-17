package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.User;
import com.ecommerce.smartphoneshop.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User save(UserDTO userDTO);
    User findByUsername(String username);
    User findByEmail(String email);
    User findByPhone(String phone);
    User updateInfo(User user, String phone, String email, String address);
    int getNewUsersThisMonth();
    User changePassword(User user, String newPassword);
}
