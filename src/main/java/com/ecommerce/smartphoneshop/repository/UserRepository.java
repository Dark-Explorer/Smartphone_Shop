package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.username = :username")
    User findByUsername(String username);
    @Query("select u from User u where u.email = :email")
    User findByEmail(String email);
    @Query("select u from User u where u.phone = :phone")
    User findByPhone(String phone);

}
