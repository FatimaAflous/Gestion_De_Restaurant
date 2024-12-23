package com.projet_restaurant.servicecommandes.Repository;

import com.projet_restaurant.servicecommandes.Entity.Order;
import com.projet_restaurant.servicecommandes.Entity.OrderStatus;
import com.rabbitmq.client.impl.LongStringHelper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    Order findByUserIdAndStatus(Long userId, OrderStatus status);
    @Query("SELECT o FROM Order o JOIN FETCH o.items")
    List<Order> findAllWithItems();
}
