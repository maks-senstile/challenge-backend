package com.example.services;

import com.example.exceptions.ProviderDeliveryException;
import com.example.model.Address;
import com.example.model.DeliveryOrder;
import com.example.model.DeliveryOrderScheduled;
import com.example.model.enums.OrderStatus;
import com.example.repositories.AddressRepository;
import com.example.repositories.DeliveryOrderRepository;
import com.example.repositories.DeliveryOrderScheduledRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class DeliveryOrderService {

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;
    @Autowired
    private DeliveryOrderScheduledRepository deliveryOrderScheduledRepository;
    @Autowired
    private OrderProcessingService orderProcessingService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private EventsService eventsService;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void create(DeliveryOrder deliveryOrder) {
        DeliveryOrder pendingDeliveryOrder = deliveryOrderRepository.save(deliveryOrder);
        eventsService.created(deliveryOrder);

        executorService.submit(() -> {
            Optional<DeliveryOrder> savedOrderOptional = deliveryOrderRepository.findById(pendingDeliveryOrder.getId());

            Address address;
            if (savedOrderOptional.isPresent()) {
                address = addressRepository.findById(savedOrderOptional.get().getAddressId()).orElse(null);
            } else {
                address = null;
            }

            if (savedOrderOptional.isPresent() && address != null) {
                DeliveryOrder savedDeliveryOrder = savedOrderOptional.get();
                try {
                    var providerOrderId = orderProcessingService.sendToProcessing(deliveryOrder, address);
                    savedDeliveryOrder.setStatus(OrderStatus.PROCESSING);
                    savedDeliveryOrder.setProviderOrderId(providerOrderId);
                    deliveryOrderRepository.save(savedDeliveryOrder);
                    eventsService.sentToProvider(savedDeliveryOrder);
                } catch (Exception e) {
                    if (e instanceof ProviderDeliveryException) {
                        savedDeliveryOrder.setStatus(OrderStatus.FAILED);
                        deliveryOrderRepository.save(savedDeliveryOrder);
                        eventsService.failedToSendToProvider(savedDeliveryOrder);
                    } else {
                        savedDeliveryOrder.setStatus(OrderStatus.INTERNAL_ERROR);
                        deliveryOrderRepository.save(savedDeliveryOrder);
                    }
                }
            }
        });
    }

    public void schedule(DeliveryOrderScheduled deliveryOrderScheduled) {
        deliveryOrderScheduledRepository.save(deliveryOrderScheduled);
        eventsService.created(deliveryOrderScheduled);
    }

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
