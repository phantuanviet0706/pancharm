package com.example.pancharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.OrderItems;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItems, Integer>, JpaSpecificationExecutor<OrderItems> {

    @Modifying
    @Query("UPDATE Products p SET p.quantity = p.quantity - :quantity where p.id = :productId and p.quantity >= :quantity")
    int decreaseStockIfEnough(@Param("productId") int productId, @Param("quantity") int quantity);
}
