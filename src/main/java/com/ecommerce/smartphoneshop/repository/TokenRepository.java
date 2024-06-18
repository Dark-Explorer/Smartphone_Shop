package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
}
