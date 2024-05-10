package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    @Query("select pi from ProductItem pi where pi.product.id = :id")
    List<ProductItem> findItemsOfProduct(Long id);

    @Query("select pi from ProductItem pi where pi.variation = :name and pi.product.id = :id")
    ProductItem findByName(String name, Long id);
}
