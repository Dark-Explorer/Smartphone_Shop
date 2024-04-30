package com.ecommerce.smartphoneshop.dto;

import com.ecommerce.smartphoneshop.domain.ProductItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private List<ProductItem> productItems;
}
