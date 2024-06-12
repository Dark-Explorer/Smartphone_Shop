package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.name = :name")
    Product findByName(String name);

    @Query("select p from Product p where p.brand = :brand")
    List<Product> findByBrand(String brand);

    @Query("select p from Product p where p.productItems[0].price >= :min and p.productItems[0].price <= :max")
    List<Product> filterByPrice(Long min, Long max);


}
