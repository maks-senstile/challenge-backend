### Technical challenge for backend developers

Assume we have a big legacy system and one of the parts is orders delivery system which was quickly implemented some time ago. 
Now we have chance to completely rewrite the system, including API change (endpoints, DTOs etc). As a technical challenge we suggest you to take it. You can do whatever you want following the acceptance criteria:

- Refactor the existing code or write it from scratch
- Use any architecture you are comfortable with
- Use modern Java or Kotlin (we use Kotlin)
- Use any database SQL/NoSQL (please use embedded)
- The code must be tested. We don't expect 100% coverage for this challenge, we want to see that you
  can write sensible tests. For example, if you have several similar converters, no need to test
  every single class/method, just enough to test one. But for critical logic like order delivery
  process we'd like to see coverage of different scenarios (happy path/failed cases)
- We expect to see SOLID principles in action
- The service should be easy to run

#### Here are business rules of the processing:

- We have a list of users (`/find-all-users` endpoint)
- A user has one or more saved addresses
- The system accepts deliveryOrder request for a user using one of his addresses
- An order can be executed (sent to a delivery provider) as soon as possible or scheduled to
  be executed later
- After the service receives a request it stores an order object in our DB and sends a request to a provider. Then the system tries to handle the order async. We just update orders status to `PROCESSING` and set an id issued by a provider. Note: for this challenge we don't care about the final response from providers
- For evey single action we have to send an event to a messaging system (e.q. kafka, sns etc). Here we just emulate it in `com.senstile.EventsService` (please don't implement a real messaging provider)
- We noticed that in current solution we are losing some outgoing events about deliveryOrders. We
  MUST 100% notify listeners regarding any statuses of orders. That means a new solution should
  be designed to cover the requirement. For example an order has been sent to a provider, we
  updated a status to `PROCESSING` in database, and then we have to send an event. What if the
  event was failed to send (e.q. connection issues to a messaging provider) but we updated the order in DB?

#### Steps to proceed:

- Fork the repository (in case you want to refactor the existing solution). Or you can create the
  project from scratch
- Implement your nice solution
- If you need to put comments/description regarding the solution please write them in `SOLUTION.md`
- Once complete invite `maks-senstile` for review
- There are no time limit to complete this challenge 
