package com.senstile.receiveordersystem.dto;

import com.senstile.receiveordersystem.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@ToString
public class CommonDeliveryOrderDto {


    private long id;
    private Long addressId;
    private Long userId;
    private List<Long> productIds;
    private Instant createdAt;
    private OrderStatus status;
    private String providerName;
    private String providerOrderId;
    private Instant executeAt;

}
