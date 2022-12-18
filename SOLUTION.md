### Solution
- Step#1: Categorize all classes by packages
- Step#2: Use hexagonal architecture

#### Step#1: Categorize all files in the project by directories
I just classified files by their stereotypes, no more. I got following tree:
```
.
├── Application.java
├── config
│   └── SwaggerConfig.java
├── exceptions
│   └── ProviderDeliveryException.java
├── model
│   ├── Address.java
│   ├── DeliveryOrder.java
│   ├── DeliveryOrderScheduled.java
│   ├── User.java
│   └── enums
│       └── OrderStatus.java
├── repositories
│   ├── AddressRepository.java
│   ├── DeliveryOrderRepository.java
│   ├── DeliveryOrderScheduledRepository.java
│   └── UserRepository.java
├── rest
│   ├── DeliveryOrderController.java
│   └── UserController.java
└── services
    ├── DeliveryOrderService.java
    ├── EventsService.java
    ├── OrderProcessingService.java
    └── processors
        ├── Provider1Processor.java
        ├── Provider2Processor.java
        └── Provider3Processor.java
```

#### Step#2: Use hexagonal architecture
I completely refactored the structure of the application. I moved domain logic into separate module, so I left integrations and
dependency on framework and orm in a runnable module with main class. But in order to finish sooner I skipped the following
steps those I won't skip in production.
- I skipped entire transaction management because the most suitable way to implement it is AOP, but it was too long to fiddle with it and then debug it.
- Locks in db. The new design allows app to run in several instances and locks are required. I suppose adding LOCK keyword would be enough but it depends on transaction management, so there was no option to add it.
- Advice logic. It would be redundant to implement it in test task.
- Most of the tests except two ones which shows my ability to write them.

In addition, I used a new architectural technique, the EventBus, that allows for indirect operations, such as running
execution of the ASAP orders as they are created. I'd like to use external queue in order to keep the app fully stateless
and the implementation allows us to introduce it in the future.

I moved OR mappings to jpa xml definitions in order to avoid dependency on any mappings.
