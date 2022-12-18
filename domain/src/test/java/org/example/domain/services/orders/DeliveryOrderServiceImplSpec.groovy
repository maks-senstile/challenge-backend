package org.example.domain.services.orders

import com.github.oxo42.stateless4j.StateMachine
import org.apache.commons.lang3.RandomStringUtils
import org.example.domain.eventbus.AbstractEvent
import org.example.domain.eventbus.EventBus
import org.example.domain.model.addresses.Address
import org.example.domain.model.orders.DeliveryOrder
import org.example.domain.model.orders.DeliveryOrderRepository
import org.example.domain.model.orders.embedded.Schedule
import org.example.domain.model.orders.enums.OrderStatus
import org.example.domain.model.users.User
import org.example.domain.model.users.UserRepository
import org.example.domain.services.orders.commands.CreateDeliveryOrderCommand
import org.example.domain.services.orders.events.OrderCreatedEvent
import org.example.domain.services.orders.processors.Processor
import org.example.domain.utils.Generator
import spock.lang.Specification

import java.time.Instant

class DeliveryOrderServiceImplSpec extends Specification {

    private Generator<Long> deliveryOrderIdGenerator
    private DeliveryOrderRepository repository
    private UserRepository userRepository
    private EventBus eventBus
    private List<Processor> availableProcessors
    private Processor simpleProcessor

    void setup() {
        deliveryOrderIdGenerator = Mock(Generator<Long>)
        deliveryOrderIdGenerator.generate() >> Long.parseLong(RandomStringUtils.randomNumeric(6))
        repository = Mock(DeliveryOrderRepository)
        userRepository = Mock(UserRepository)
        eventBus = Mock(EventBus)
        simpleProcessor = Mock(Processor)
        availableProcessors = new ArrayList<>()
    }

    def "Scenario: Service successfully creates order"() {
        given: "Service, user, creation command"
        DeliveryOrderServiceImpl service = targetService()
        User user = randomUser()
        CreateDeliveryOrderCommand command = createOrderCommandStub(user)

        AbstractEvent firedEvent = null
        DeliveryOrder savedOrder = null

        when: "Creating order"
        DeliveryOrder order = service.create(command)

        then: "Order created, event fired"
        // @formatter:off
        1 * userRepository.findById(_)  >> Optional.of(user)
        1 * repository.save(_)          >> { args -> { savedOrder = (DeliveryOrder) args[0] } }
        1 * eventBus.publish(_)         >> { args -> { firedEvent = (OrderCreatedEvent) args[0] } }
        // @formatter:on

        Objects.nonNull(firedEvent)
                && firedEvent instanceof OrderCreatedEvent
                && Objects.equals(firedEvent.getOrderId(), order.getId())

        Objects.nonNull(savedOrder)
                && savedOrder.is(order)

        // @formatter:off
        Objects.nonNull(order)
                && order.getId() == deliveryOrderIdGenerator.generate()
                && order.getUserId() == command.getUserId()
                && Objects.nonNull(order.getAddress())
                        && order.getAddress().getId() == command.getAddressId()
                && Objects.nonNull(order.getCreatedAt())
                && Objects.isNull(order.getProcessor())
                && Objects.isNull(order.getProviderOrderId())
                && Objects.equals(order.getSchedule(), command.getSchedule())
                && Objects.equals(order.getProductIds(), command.getProductIds())
        // @formatter:on
    }

    def "Scenario: Service successfully executes order"() {
        given: "Service, ASAP order"
        DeliveryOrderServiceImpl service = targetService()
        service.availableProcessors.push(simpleProcessor)

        DeliveryOrder order = validAsapOrderWithoutState()
        order.stateMachine = new StateMachine<>(OrderStatus.PENDING, DeliveryOrder.stateMachineConfig)

        DeliveryOrder processedOrder = null
        DeliveryOrder savedOrder = null

        when: "Executing order"
        service.execute(order)

        then:
        // @formatter:off
        1 * simpleProcessor.canAccept(_) >> true
        1 * simpleProcessor.process(_)   >> { args -> { processedOrder = args[0] } }
        1 * repository.save(_)           >> { args -> { savedOrder = args[0] } }
        // @formatter:on

        Objects.nonNull(processedOrder)
                && savedOrder.is(order)

        Objects.nonNull(savedOrder)
                && savedOrder.is(order)
    }

    def "Scenario: Service throws exception if no suitable processor was found"() {
        given: "Service, ASAP order"
        DeliveryOrderServiceImpl service = targetService()
        service.availableProcessors.push(simpleProcessor)

        DeliveryOrder order = validAsapOrderWithoutState()
        order.stateMachine = new StateMachine<>(OrderStatus.PENDING, DeliveryOrder.stateMachineConfig)

        DeliveryOrder savedOrder = null

        when: "Executing order"
        service.execute(order)

        then:
        // @formatter:off
        1 * simpleProcessor.canAccept(_) >> false
        0 * simpleProcessor.process(_)
        1 * repository.save(_)           >> { args -> { savedOrder = args[0] } }
        // @formatter:on

        Objects.nonNull(savedOrder)
                && savedOrder.is(order)
                && savedOrder.getStatus() == OrderStatus.INTERNAL_ERROR
    }

    private DeliveryOrderServiceImpl targetService() {
        new DeliveryOrderServiceImpl(
                deliveryOrderIdGenerator,
                repository,
                userRepository,
                eventBus,
                availableProcessors
        )
    }

    private CreateDeliveryOrderCommand createOrderCommandStub(User user) {
        def products = Arrays.asList(Long.parseLong(RandomStringUtils.randomNumeric(6)))
        def command = Stub(CreateDeliveryOrderCommand)
        command.getUserId() >> user.getId()
        command.getAddressId() >> user.getAddresses().get(0).getId()
        command.getSchedule() >> Schedule.executeAsap()
        command.getProductIds() >> products
        return command
    }

    private User randomUser() {
        new User(
                Long.parseLong(RandomStringUtils.randomNumeric(6)),
                RandomStringUtils.randomAlphabetic(12),
                Arrays.asList(
                        new Address(
                                Long.parseLong(RandomStringUtils.randomNumeric(6)),
                                RandomStringUtils.randomAlphabetic(12),
                                RandomStringUtils.randomAlphabetic(12),
                                RandomStringUtils.randomAlphabetic(12),
                                RandomStringUtils.randomAlphabetic(12)
                        )
                )
        )
    }

    private DeliveryOrder validAsapOrderWithoutState() {
        def order = new DeliveryOrder(
                Long.parseLong(RandomStringUtils.randomNumeric(6)),
                new Address(
                        Long.parseLong(RandomStringUtils.randomNumeric(6)),
                        RandomStringUtils.randomAlphanumeric(16),
                        RandomStringUtils.randomAlphanumeric(16),
                        RandomStringUtils.randomAlphanumeric(16),
                        RandomStringUtils.randomAlphanumeric(16)
                ),
                Long.parseLong(RandomStringUtils.randomNumeric(6)),
                Collections.singletonList(Long.parseLong(RandomStringUtils.randomNumeric(6))),
                Instant.now(),
                null,
                null,
                null,
                Schedule.executeAsap()
        )
    }
}
