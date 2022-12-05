## Structure
Project consists of several modules

* users-service - user management
* delivery-service - the main business login
... modules under development
* delivery-adapter-x - adapter for partner system (under development)
* delivery-adapter-y - adapter for partner system (under development)
* delivery-adapter-z - adapter for partner system (under development)

## How to start
Java 11 is required
### Pure java
maven clean package

java -jar users-service\target\users-service-1.0-jar-with-dependencies.jar
`http://127.0.0.1:8081/find-user-by-id/1`

java -jar delivery-service\target\delivery-service-1.0-jar-with-dependencies.jar
### With docker
change `user-service-url` property in `delivery-service/resource/application.yaml` to "http://users-service:8081" 
maven clean package
docker compose up

## Using
##### Get user
`http://127.0.0.1:8081/find-user-by-id/1`
##### Get all users
`http://localhost:8081/find-all-users`
##### Create order
```bash
curl --request POST \
  --url 'http://127.0.0.1:8080/delivery/create-new-delivery-order?execute_at=ASAP' \
  --header 'Content-Type: application/json' \
  --data '{
	"user_id": 1,
	"address_id": 1,
	"product_ids": [50, 42]
}'
```
#### Find orders
`http://127.0.0.1:8080/delivery/find-all-delivery-orders`


## Description
There is a classic java application. I am using Kotlin as main language and [ktor](https://ktor.io/) as web framework.
Database layer implemented with [jooq](https://www.jooq.org/).

There are several solutions for writing into DB and send message 100% reliable:
* Using outbox pattern with Debezium. You can read more in the book with a pig.
* Using PostgreSQL extension pg_amp. This extension send message at last phase of the transaction.
* Using queue specific features like RabbitMQ publisher confirms. https://www.rabbitmq.com/confirms.html You send the messages, waiting confirmation from Rabbit and write data after that. If write data fails, you repeat the process. (This can produce duplicate massages.)

We are using the first one.