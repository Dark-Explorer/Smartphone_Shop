package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, ListCrudRepository<Product, Long> {
    @Query("select p from Product p where p.name = :name")
    Product findByName(String name);

    @Query("select p from Product p where p.id = :id")
    Product getReferenceById(Long id);

    @Query("select p from Product p where p.name like %?1%")
    List<Product> findByKeyword(String keyword);
}
