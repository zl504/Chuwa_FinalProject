# E-commerce Microservices

This project demonstrates an e-commerce system built with a microservices architecture.
It includes four main services:

* Item Service (with Inventory)

* Order Service

* Payment Service

* Account Service




User flow
1. Account Service – Register and log in (JWT authentication).

2. Order Service – Place an order for items.

3. Item Service – Retrieve item details (availability, price, etc.).

    * Create Order

    * Update Order

    * Cancel Order

4. Payment Service – Process payment for the order.

    * Ensures idempotency (no duplicate payments1. ).




## Item Service:

**Database: MongoDB**

**Base API URL:**  `../api/items`

The Item Service manages product data and inventory. It supports flexible schemas using MongoDB.

The Item Service manages all item-related information, including:

* Unit price

* Item name

* Product images (URLs)

* UPC (Universal Product Code)

* Item ID

* Inventory Availabilty

It stores comprehensive metadata about each item. Since the metadata structure can vary, MongoDB is used for its flexibility in handling dynamic schemas.

In addition to item details, the service handles inventory lookup and updates, allowing clients to check the remaining available units of a product.

Endpoints
* GET `/api/items` – Retrieve all items

* GET `/api/items/{id}` – Retrieve item details by ID

* POST `/api/items` – Create a new item in MongoDB

* DELETE `/api/items/{id}` – Delete an item by ID

* PUT `/api/items/{id}` – Update an existing item by ID

Inventory Endpoints

* GET `/api/items/{id}/availability ` – Check available stock for an item

* PUT `/api/items/{id}/availability/increase?qty=` – increase the availability

* PUT `/api/items/{id}/availability/decrease?qty=` – decrease the availability


## Order Service:

**Database: Cassandra**

**Messaging: Kafka (Producer & Consumer)**

The Order Service handles the creation, updating, cancellation, and retrieval of orders. It supports both synchronous RESTful API calls and asynchronous messaging through Kafka, enabling integration with other services.

Order Flow:
* Create Order (right after user clicks the submit button, an order will be created)
* Cancel Order
* Update Order

Order Service APIs:
* Create Order
* Cancel Order
* Update Order
* Order Lookup

While RESTful APIs remain stateless, each order itself has a state, for example.


`Order Create, Order Paid, Order Completed, Order Cancel`

Consider using **Cassandra** database for order service, and design a reasonable schema for
order information.

Please note that above APIs provide a synchronous approach, order service should publish
order information to some other services...

**Base API URL:**  `../api/orders`

This service manages Order including create order, cancel order, see the order details by user id.

Endpoints

* POST `/api/orders` – Create a new order and store it in Cassandra.

* GET `/api/orders/{id}` - Retrieve order details by order ID

* GET `/api/orders/by-user/{userId}` – Retrieve all orders for a given user
* POST `/api/orders/{id}/cancel` – Cancel an order.



## Payment Service:

**Database: MySQL(transaction integrity)**

integrates with order service, provides REST APIs, and publish payment transaction
results to consumers.
* Submit Payment
* Update Payment
* Reverse Payment: Refund
* Payment Lookup: lookup a payment, return its status
Idempotency should be guaranteed throughout the payment flow, as we don't want to
double-charge customers or double-refund them.


1. Order Service

* Creates order with status PENDING_PAYMENT.

* May reserve inventory.

2. Payment Service

* Accepts payment request for an order.

* If payment successful:

    * Payment Service marks payment record SUCCESS.

    * Publishes a PaymentSucceeded event (Kafka).

    * Order Service listens for that event → updates order to PAID or COMPLETED .

* If payment fails:

    * Payment Service marks payment record FAILED.

    * Publishes a PaymentFailed event.

    * Order Service sets order status to PAYMENT_FAILED.




## Account Service:

**Database: MySQL(transaction integrity)**

* Create Account
* Update Account
* Account Lookup
* ...

This service manages user accounts: registration, login (JWT), profile retrieval, and profile updates (addresses & payment method).

Users should be able to create/update their account with following necessary information:

* id

* email

* username

* password

* shippingAddress

* billingAddress

* paymentMethod

**Base API URL:**  

* `../auth`– authentication endpoints (public)
* `../account`– account/profile endpoints (require JWT)

This service manages user accounts, including registration, login, and account detail retrieval.

Endpoints
* POST `/auth/register` – Create a new account and store it in MySQL.

* POST `/auth/login` – Log in to the account and receive a JWT token.
    * Why need this token?

        * The user logs in once → your service verifies credentials and issues a JWT.

        * The token contains encoded info (like the username, issued time, expiry).

        * The client stores this token (e.g., in browser localStorage or in an app).

        * For future requests (like /auth/me, /order, /payment), the client just sends the token in the Authorization header.

        * Your backend verifies the token’s signature & expiry — no need to recheck the database for credentials.

* GET `/account/me` – Retrieve details of the currently authenticated account (requires valid JWT).

* PATCH `/account/update` – Update the Account including billing address, shipping address, and payment method.




Event Flow (Kafka Integration)

1. Order Service places an order → publishes OrderPlacedEvent (order.placed).

2. Payment Service consumes the event → processes payment.

3. Payment Service publishes PaymentSucceeded or PaymentFailed (payments.events).

4. Order Service consumes payments.events:

    * If success → mark order as PAID in Cassandra.

    * If failure → mark order as CANCELLED and restore inventory (itemClient.increaseAvailability(...)).