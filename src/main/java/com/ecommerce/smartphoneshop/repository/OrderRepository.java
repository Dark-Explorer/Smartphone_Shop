package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<ShopOrder, Long> {
}
