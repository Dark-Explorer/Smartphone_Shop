package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("select p from Product p where p.name = :name")
    Product findByName(String name);
}
