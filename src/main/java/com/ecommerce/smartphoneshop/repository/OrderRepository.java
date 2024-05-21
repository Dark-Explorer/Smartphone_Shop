package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ShopOrder, Long> {
    @Query("select o from ShopOrder o where o.status = 'CREATED' or o.status = 'DELIVERING'")
    List<ShopOrder> getPendingOrders();

    @Query("select o from ShopOrder o where o.status = 'DELIVERED' or o.status = 'CANCELLED'")
    List<ShopOrder> getCompletedOrders();
}
