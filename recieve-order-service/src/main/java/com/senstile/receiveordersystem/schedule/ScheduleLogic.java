package com.senstile.receiveordersystem.schedule;

import com.senstile.receiveordersystem.enums.OrderStatus;
import com.senstile.receiveordersystem.exception.ProviderDeliveryException;
import com.senstile.receiveordersystem.model.Address;
import com.senstile.receiveordersystem.model.DeliveryOrderScheduled;
import com.senstile.receiveordersystem.repository.AddressRepository;
import com.senstile.receiveordersystem.repository.DeliveryOrderScheduledRepository;
import com.senstile.receiveordersystem.services.EventsService;
import com.senstile.receiveordersystem.services.OrderProcessingService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@AllArgsConstructor
@Component
public class ScheduleLogic {

    private final DeliveryOrderScheduledRepository deliveryOrderScheduledRepository;
    private final AddressRepository addressRepository;
    private final EventsService eventsService;
    private final OrderProcessingService orderProcessingService;

    @Scheduled(fixedDelay = 5000)
    public void run() {
        deliveryOrderScheduledRepository.findAllByExecuteAtBefore(Instant.now())
                .forEach(this::processScheduled);
    }

    private void processScheduled(DeliveryOrderScheduled deliveryOrderScheduled) {
        Address address = addressRepository.findById(deliveryOrderScheduled.getAddressId()).orElse(null);
        if (address != null) {
            try {
                var providerOrderId = orderProcessingService.sendToProcessing(deliveryOrderScheduled, address);
                deliveryOrderScheduled.setStatus(OrderStatus.PROCESSING);
                deliveryOrderScheduled.setProviderOrderId(providerOrderId);
                deliveryOrderScheduledRepository.save(deliveryOrderScheduled);
                eventsService.sentToProvider(deliveryOrderScheduled);
            } catch (Exception e) {
                if (e instanceof ProviderDeliveryException) {
                    deliveryOrderScheduled.setStatus(OrderStatus.FAILED);
                    deliveryOrderScheduledRepository.save(deliveryOrderScheduled);
                    eventsService.failedToSendToProvider(deliveryOrderScheduled);
                } else {
                    deliveryOrderScheduled.setStatus(OrderStatus.INTERNAL_ERROR);
                    deliveryOrderScheduledRepository.save(deliveryOrderScheduled);
                }
            }
        }
    }
}
