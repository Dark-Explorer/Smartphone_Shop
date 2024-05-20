package com.ecommerce.smartphoneshop.repository;

import com.ecommerce.smartphoneshop.domain.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    @Query("select ol from OrderLine ol where ol.shopOrder.id = :id")
    List<OrderLine> getOrderLineByOrderId(Long id);
}
