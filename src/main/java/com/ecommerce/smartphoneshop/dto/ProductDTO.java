package com.ecommerce.smartphoneshop.dto;

import com.ecommerce.smartphoneshop.domain.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String brand;
    private String specification;
    private String image;
    private int warranty;
    private boolean is_active = true;
}
