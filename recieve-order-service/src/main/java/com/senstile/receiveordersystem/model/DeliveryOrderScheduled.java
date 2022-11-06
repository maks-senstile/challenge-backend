package com.senstile.receiveordersystem.model;


import com.senstile.receiveordersystem.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@Entity(name = "delivery_orders_scheduled")
public class DeliveryOrderScheduled {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long addressId;
    private Long userId;
    @ElementCollection(targetClass = Long.class)
    private List<Long> productIds;
    private Instant createdAt;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String providerName;
    private String providerOrderId;
    private Instant executeAt;

}
