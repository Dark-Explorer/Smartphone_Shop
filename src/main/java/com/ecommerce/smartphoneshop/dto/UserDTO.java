package com.ecommerce.smartphoneshop.dto;

import com.ecommerce.smartphoneshop.domain.ShopOrder;
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
    private String email;
    private String username;
    @Size(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    private String password;
    private String address;
    private List<ShopOrder> orders;
}
