package com.ecommerce.smartphoneshop.dto;

import com.ecommerce.smartphoneshop.domain.ShopOrder;
import com.ecommerce.smartphoneshop.domain.ShoppingCart;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String phone;
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotBlank(message = "Chưa điền tên đăng nhập")
    private String username;
    @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    private String password;
    private String repeatPassword;
    private String address;
    private List<ShopOrder> orders;
    private ShoppingCart shoppingCart;
}
