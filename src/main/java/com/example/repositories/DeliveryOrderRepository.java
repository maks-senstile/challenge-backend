package com.example.repositories;

import com.example.model.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {
}
