package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<ShopOrder, Long> {
    @Query("select o from ShopOrder o where o.status = 'CREATED' or o.status = 'DELIVERING'")
    List<ShopOrder> getPendingOrders();

    @Query("select o from ShopOrder o where o.status = 'DELIVERED' or o.status = 'CANCELLED'")
    List<ShopOrder> getCompletedOrders();

    @Query("select sum(o.total) from ShopOrder o where o.status = 'DELIVERED' and o.date_created between :startDate and :endDate")
    Long getIncomeForDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("select count(o) from ShopOrder o where o.status = 'CREATED'")
    int getNumberOfPendingOrders();

    @Query("select count(o) from ShopOrder o where o.status = 'DELIVERED' and o.date_created between :startDate and :endDate")
    int getCompletedOrdersInRange(LocalDateTime startDate, LocalDateTime endDate);
}
