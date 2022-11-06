package com.senstile.receiveordersystem.services;

import com.senstile.receiveordersystem.aspect.LogExecutionTime;
import com.senstile.receiveordersystem.config.CustomExecutorService;
import com.senstile.receiveordersystem.dto.CommonDeliveryOrderDto;
import com.senstile.receiveordersystem.enums.OrderStatus;
import com.senstile.receiveordersystem.exception.ProviderDeliveryException;
import com.senstile.receiveordersystem.model.Address;
import com.senstile.receiveordersystem.model.DeliveryOrder;
import com.senstile.receiveordersystem.model.DeliveryOrderScheduled;
import com.senstile.receiveordersystem.repository.AddressRepository;
import com.senstile.receiveordersystem.repository.DeliveryOrderRepository;
import com.senstile.receiveordersystem.repository.DeliveryOrderScheduledRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.senstile.receiveordersystem.utils.ConstantsUtils.ASAP;

@Service
@AllArgsConstructor
public class DeliveryOrderService {

    private final DeliveryOrderRepository deliveryOrderRepository;
    private final DeliveryOrderScheduledRepository deliveryOrderScheduledRepository;
    private final OrderProcessingService orderProcessingService;
    private final AddressRepository addressRepository;
    private final EventsService eventsService;
    private final UserService userService;
    private final ConversionService conversionService;

    @CustomExecutorService
    private final ExecutorService executorService;

    public ResponseEntity executeLogic(String userId, String addressId, String executeAt, String productIds) {

        try {
            userService.findById(parseParamToLong(userId));
        } catch (Exception e) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        if (!findById(parseParamToLong(addressId)).isPresent()) {
            return new ResponseEntity("Payment method not found", HttpStatus.NOT_FOUND);
        }

        if (executeAt.equals(ASAP)) {
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setUserId(parseParamToLong(userId));
            deliveryOrder.setAddressId(parseParamToLong(addressId));
            deliveryOrder.setProductIds(Arrays.stream(productIds.split(",")).map(id -> parseParamToLong(id)).collect(Collectors.toList()));
            deliveryOrder.setCreatedAt(Instant.now());
            deliveryOrder.setStatus(OrderStatus.PENDING);
            create(deliveryOrder);

            CommonDeliveryOrderDto result = conversionService.convert(deliveryOrder, CommonDeliveryOrderDto.class);

            return new ResponseEntity(result, HttpStatus.OK);

        } else {
            DeliveryOrderScheduled deliveryOrderScheduled = new DeliveryOrderScheduled();

            deliveryOrderScheduled.setUserId(parseParamToLong(userId));
            deliveryOrderScheduled.setAddressId(parseParamToLong(addressId));
            deliveryOrderScheduled.setProductIds(Arrays.stream(productIds.split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
            deliveryOrderScheduled.setCreatedAt(Instant.now());
            deliveryOrderScheduled.setExecuteAt(Instant.parse(executeAt));
            deliveryOrderScheduled.setStatus(OrderStatus.PENDING);

            schedule(deliveryOrderScheduled);

            CommonDeliveryOrderDto result = conversionService.convert(deliveryOrderScheduled, CommonDeliveryOrderDto.class);

            return new ResponseEntity(result, HttpStatus.OK);
        }
    }

    @LogExecutionTime
    public Optional<Address> findById(long id) {
        return addressRepository.findById(id);
    }

    @LogExecutionTime
    public List<CommonDeliveryOrderDto> collectAllAndReturn() {
        List<DeliveryOrder> deliveryOrders = deliveryOrderRepository.findAll();
        List<DeliveryOrderScheduled> ordersScheduled = deliveryOrderScheduledRepository.findAll();

        List<CommonDeliveryOrderDto> deliveryOrdersConverted = deliveryOrders.stream()
                .map(item -> conversionService.convert(item, CommonDeliveryOrderDto.class))
                .collect(Collectors.toList());
        List<CommonDeliveryOrderDto> ordersScheduledConverted = ordersScheduled.stream()
                .map(item -> conversionService.convert(item, CommonDeliveryOrderDto.class))
                .collect(Collectors.toList());

        return Stream.concat(deliveryOrdersConverted.stream(), ordersScheduledConverted.stream()).collect(Collectors.toList());
    }

    private Long parseParamToLong(String id) {
        return Long.parseLong(id);
    }

    private void create(DeliveryOrder deliveryOrder) {
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

    private void schedule(DeliveryOrderScheduled deliveryOrderScheduled) {
        deliveryOrderScheduledRepository.save(deliveryOrderScheduled);
        eventsService.created(deliveryOrderScheduled);
    }

}
