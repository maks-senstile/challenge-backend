Project was split into several modules

users-service - user management
delivery-service - the main business login
delivery-adapter-x - adapter for partner system (under development)
delivery-adapter-y - adapter for partner system (under development)
delivery-adapter-z - adapter for partner system (under development)

## Запуск
(docker does not implemented)

maven clean package

java -jar users-service\target\users-service-1.0-jar-with-dependencies.jar
`http://127.0.0.1:8080/find-user-by-id/1`

## Описание
Synchronous and Asynchronous call use the same buiseness login and event.
The only different is the [channel](https://www.enterpriseintegrationpatterns.com/MessageChannel.html) implementation.
The first one is blocking channel and the second one is using message queue under the hood.
Both channel deliver events to the appropriate processors.

Для каждого партнера мы будем создавать свой адаптер, потому что технологии и протоколы работы с ними часто очень разные.
Ну и меняются они независимо. (У сервиса должна быть одна причина для изменения).

There are several solutions for writing into DB and send message 100% reliable:
* Using outbox pattern with Debezium. You can read more in the book with a pig.
* Using PostgreSQL extension pg_amp. This extension send message at last phase of the transaction.
* Using queue specific features like RabbitMQ publisher confirms. https://www.rabbitmq.com/confirms.html You send the messages, waiting confirmation from Rabbit and write data after that. If write data fails, you repeat the process. (This can produce duplicate massages.)

We are using the first one.