### Solution
- Step#1: Categorize all classes by packages
- Step#2: Design Domain model
- Step#3: Define interfaces for integration with related systems e.g. event system & DB
- Step#4: Create service layer
- Step#5: Bring project into modules like following: domain, service, scheduler etc.
- Step#6: Foresee DB locks in advance, because both service and scheduler apps are designed for running in separate instances but with the same schema
- Step#7: Cover with tests

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