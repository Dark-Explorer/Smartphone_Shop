package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.domain.ProductItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductItemService {
    List<ProductItem> getItemsOfProduct(Long id);
    ProductItem saveProductItem(ProductItem productItem);
    ProductItem updateProductItem(ProductItem productItem);
    void deleteProductItem(Long id);
}
