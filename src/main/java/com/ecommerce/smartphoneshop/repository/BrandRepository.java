package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, String> {
    @Query("select b from Brand b where b.name = :name")
    Brand findByName(String name);
}
