package com.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface DeliveryOrderScheduledRepository extends JpaRepository<DeliveryOrderScheduled, Long> {

    List<DeliveryOrderScheduled> findAllByExecuteAtBefore(Instant date);
}
