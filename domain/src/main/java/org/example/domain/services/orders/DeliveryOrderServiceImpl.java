package org.example.domain.services.orders;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.domain.eventbus.EventBus;
import org.example.domain.exceptions.EntityNotFoundDomainException;
import org.example.domain.exceptions.NoSuitableProcessorDomainException;
import org.example.domain.model.addresses.Address;
import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.model.orders.DeliveryOrderRepository;
import org.example.domain.model.orders.embedded.Schedule;
import org.example.domain.model.orders.enums.OrderStatusTrigger;
import org.example.domain.model.users.User;
import org.example.domain.model.users.UserRepository;
import org.example.domain.services.orders.commands.CreateDeliveryOrderCommand;
import org.example.domain.services.orders.events.OrderCreatedEvent;
import org.example.domain.services.orders.processors.Processor;
import org.example.domain.utils.Generator;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class DeliveryOrderServiceImpl implements DeliveryOrderService {

    private final Generator<Long> deliveryOrderIdGenerator;
    private final DeliveryOrderRepository repository;
    private final UserRepository userRepository;
    private final EventBus eventBus;
    private final List<Processor> availableProcessors;

    @Override
    public List<DeliveryOrder> getScheduledOrders(long offset, int count) {
        return repository.findScheduledOrders(offset, count);
    }

    @Override
    public List<DeliveryOrder> getAllOrders(long offset, int count) {
        return repository.findAllOrders(offset, count);
    }

    @Override
    public DeliveryOrder create(CreateDeliveryOrderCommand command) {
        User user = getUserByIdOrThrow(command.getUserId());
        Address address = getAddressByIdOrThrow(user, command.getAddressId());
        DeliveryOrder deliveryOrder = DeliveryOrder.create(
                deliveryOrderIdGenerator,
                CreateDeliveryOrderConstructionOptionsImpl.create(command, user, address)
        );
        repository.save(deliveryOrder);
        eventBus.publish(OrderCreatedEvent.createFromOrder(deliveryOrder));
        return deliveryOrder;
    }

    @Override
    public void execute(DeliveryOrder deliveryOrder) {
        try{
            Processor processor = detectProcessor(deliveryOrder);
            processor.process(deliveryOrder);
        } catch(NoSuitableProcessorDomainException e) {
            deliveryOrder.fire(OrderStatusTrigger.SUITABLE_PROCESSOR_NOT_FOUND);
        } finally {
            repository.save(deliveryOrder);
        }
    }

    private User getUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(EntityNotFoundDomainException::new);
    }

    private Address getAddressByIdOrThrow(User user, Long id) {
        return user.getAddresses().stream()
                .filter(a -> Objects.equals(a.getId(), id))
                .findFirst()
                .orElseThrow(EntityNotFoundDomainException::new);
    }

    private Processor detectProcessor(DeliveryOrder deliveryOrder) {
        return this.availableProcessors.stream()
                .filter(processor -> processor.canAccept(deliveryOrder.getAddress()))
                .findFirst()
                .orElseThrow(NoSuitableProcessorDomainException::new);
    }

    @AllArgsConstructor(staticName = "create")
    private static class CreateDeliveryOrderConstructionOptionsImpl implements DeliveryOrder.CreateDeliveryOrderConstructionOptions {
        private final CreateDeliveryOrderCommand serviceEvent;
        private final User user;
        private final Address address;

        @Override
        public User getUser() {
            return user;
        }

        @Override
        public Address getAddress() {
            return address;
        }

        @Override
        public Schedule getSchedule() {
            return serviceEvent.getSchedule();
        }

        @Override
        public List<Long> getProductIds() {
            return serviceEvent.getProductIds();
        }
    }
}
