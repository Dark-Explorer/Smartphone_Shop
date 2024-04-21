package com.ecommerce.smartphoneshop.domain;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    @Email
    private String email;
    @Column(unique = true)
    @NotNull
    private String username;
    @NotNull
    private String password;
    private String address;
    private LocalDateTime date_signup;
    private int check_user = 1;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    private ShoppingCart shoppingCart;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<ShopOrder> orders;
}
