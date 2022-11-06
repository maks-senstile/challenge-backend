package com.senstile.receiveordersystem.services;

import com.google.common.util.concurrent.MoreExecutors;
import com.senstile.receiveordersystem.dto.CommonDeliveryOrderDto;
import com.senstile.receiveordersystem.enums.OrderStatus;
import com.senstile.receiveordersystem.exception.ProviderDeliveryException;
import com.senstile.receiveordersystem.model.Address;
import com.senstile.receiveordersystem.model.DeliveryOrder;
import com.senstile.receiveordersystem.model.DeliveryOrderScheduled;
import com.senstile.receiveordersystem.model.User;
import com.senstile.receiveordersystem.repository.AddressRepository;
import com.senstile.receiveordersystem.repository.DeliveryOrderRepository;
import com.senstile.receiveordersystem.repository.DeliveryOrderScheduledRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeliveryOrderServiceTest {

    @Mock
    private DeliveryOrderRepository mockDeliveryOrderRepository;
    @Mock
    private DeliveryOrderScheduledRepository mockDeliveryOrderScheduledRepository;
    @Mock
    private OrderProcessingService mockOrderProcessingService;
    @Mock
    private AddressRepository mockAddressRepository;
    @Mock
    private EventsService mockEventsService;
    @Mock
    private UserService mockUserService;
    @Mock
    private ConversionService mockConversionService;

    private DeliveryOrderService deliveryOrderServiceUnderTest;

    @BeforeEach
    void setUp() {
        deliveryOrderServiceUnderTest = new DeliveryOrderService(mockDeliveryOrderRepository, mockDeliveryOrderScheduledRepository,
                mockOrderProcessingService, mockAddressRepository, mockEventsService, mockUserService, mockConversionService,
                MoreExecutors.newDirectExecutorService());
    }

    @Test
    void testExecuteLogic() {

        deliveryOrderServiceUnderTest.executeLogic("userId", "addressId", "executeAt", "productIds");


        verify(mockUserService, never()).findById(0L);
        verify(mockEventsService, never()).created(new DeliveryOrder());
        verify(mockEventsService, never()).sentToProvider(new DeliveryOrder());
        verify(mockDeliveryOrderScheduledRepository, never()).save(new DeliveryOrderScheduled());
        verify(mockEventsService, never()).created(new DeliveryOrderScheduled());
    }

    @Test
    void testExecuteLogic_AddressRepositoryReturnsAbsent() {

        final User user = new User();
        user.setId(0L);
        user.setFirstName("firstName");
        final Address address = new Address();
        address.setId(0L);
        address.setAddressLine("addressLine");
        address.setCity("city");
        address.setCountry("country");
        address.setPostalCode("postalCode");
        user.setAddresses(List.of(address));

        when(mockUserService.findById(anyLong())).thenReturn(user);

        when(mockAddressRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Run the test
        deliveryOrderServiceUnderTest.executeLogic("123", "123", "1234", "123,123");

        // Verify the results
        verify(mockUserService).findById(anyLong());
    }


    @Test
    void testFindById() {
        // Setup
        final Address address = new Address();
        address.setId(0L);
        final User user = new User();
        user.setId(0L);
        user.setFirstName("firstName");
        user.setAddresses(List.of(new Address()));
        address.setUser(user);
        address.setAddressLine("addressLine");
        address.setCity("city");
        address.setCountry("country");
        address.setPostalCode("postalCode");
        final Optional<Address> expectedResult = Optional.of(address);

        // Configure AddressRepository.findById(...).
        final Address address2 = new Address();
        address2.setId(0L);
        final User user1 = new User();
        user1.setId(0L);
        user1.setFirstName("firstName");
        user1.setAddresses(List.of(new Address()));
        address2.setUser(user1);
        address2.setAddressLine("addressLine");
        address2.setCity("city");
        address2.setCountry("country");
        address2.setPostalCode("postalCode");
        final Optional<Address> address1 = Optional.of(address2);
        when(mockAddressRepository.findById(0L)).thenReturn(address1);

        // Run the test
        final Optional<Address> result = deliveryOrderServiceUnderTest.findById(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindById_AddressRepositoryReturnsAbsent() {
        // Setup
        when(mockAddressRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        final Optional<Address> result = deliveryOrderServiceUnderTest.findById(0L);

        // Verify the results
        assertThat(result).isEmpty();
    }

    @Test
    void testCollectAllAndReturn() {

        final DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setId(0L);
        deliveryOrder.setAddressId(0L);
        deliveryOrder.setUserId(0L);
        deliveryOrder.setProductIds(List.of(0L));
        deliveryOrder.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        deliveryOrder.setStatus(OrderStatus.PENDING);
        deliveryOrder.setProviderName("providerName");
        deliveryOrder.setProviderOrderId("providerOrderId");
        final List<DeliveryOrder> deliveryOrders = List.of(deliveryOrder);
        when(mockDeliveryOrderRepository.findAll()).thenReturn(deliveryOrders);

        final DeliveryOrderScheduled deliveryOrderScheduled = new DeliveryOrderScheduled();
        deliveryOrderScheduled.setId(0L);
        deliveryOrderScheduled.setAddressId(0L);
        deliveryOrderScheduled.setUserId(0L);
        deliveryOrderScheduled.setProductIds(List.of(0L));
        deliveryOrderScheduled.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        deliveryOrderScheduled.setStatus(OrderStatus.PENDING);
        deliveryOrderScheduled.setProviderName("providerName");
        deliveryOrderScheduled.setProviderOrderId("providerOrderId");
        deliveryOrderScheduled.setExecuteAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        final List<DeliveryOrderScheduled> deliveryOrderScheduleds = List.of(deliveryOrderScheduled);
        when(mockDeliveryOrderScheduledRepository.findAll()).thenReturn(deliveryOrderScheduleds);

        // Run the test
        final List<CommonDeliveryOrderDto> result = deliveryOrderServiceUnderTest.collectAllAndReturn();

        // Verify the results
        assertThat(result.size()).isEqualTo(2);
    }

}
