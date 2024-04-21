package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User findByUsername(String username);
    public User findByEmail(String email);
}
