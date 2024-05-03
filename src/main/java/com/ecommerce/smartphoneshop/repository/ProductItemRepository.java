package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.Product;
import com.ecommerce.smartphoneshop.domain.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    @Query("select pi from ProductItem pi where pi.product.id = :id")
    List<ProductItem> findItemsOfProduct(Long id);

    @Query("select pi from ProductItem pi where pi.variation = :name")
    ProductItem findByName(String name);
}
