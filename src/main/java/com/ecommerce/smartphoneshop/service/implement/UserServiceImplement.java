package com.ecommerce.smartphoneshop.service.implement;

import com.ecommerce.smartphoneshop.domain.User;
import com.ecommerce.smartphoneshop.dto.UserDTO;
import com.ecommerce.smartphoneshop.repository.UserRepository;
import com.ecommerce.smartphoneshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImplement implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplement(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}
