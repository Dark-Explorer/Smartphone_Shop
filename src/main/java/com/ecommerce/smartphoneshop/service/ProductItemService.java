package com.ecommerce.smartphoneshop.service;

import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.domain.ProductItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductItemService {
    List<ProductItem> getItemsOfProduct(Long id);
    ProductItem saveProductItem(String sku, String variation, int qtyInStock, String image, Long price, Product product);
    ProductItem updateProductItem(Product product, ProductItem productItem, String sku, String variation, int qtyInStock, String image, Long price);
    void deleteProductItem(Long id);
    ProductItem findByName(String name, Long productId);
    ProductItem findById(Long id);
}
