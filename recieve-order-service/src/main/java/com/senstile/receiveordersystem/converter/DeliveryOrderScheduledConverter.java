package com.senstile.receiveordersystem.converter;

import com.senstile.receiveordersystem.dto.CommonDeliveryOrderDto;
import com.senstile.receiveordersystem.model.DeliveryOrder;
import com.senstile.receiveordersystem.model.DeliveryOrderScheduled;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class DeliveryOrderScheduledConverter implements Converter<DeliveryOrderScheduled, CommonDeliveryOrderDto> {

    @Override
    public CommonDeliveryOrderDto convert(DeliveryOrderScheduled source) {
        return CommonDeliveryOrderDto.builder()
                .id(source.getId())
                .addressId(source.getAddressId())
                .userId(source.getUserId())
                .productIds(source.getProductIds())
                .createdAt(source.getCreatedAt())
                .status(source.getStatus())
                .providerName(source.getProviderName())
                .providerOrderId(source.getProviderOrderId())
                .executeAt(source.getExecuteAt())
                .build();
    }
}
