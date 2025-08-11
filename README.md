# Chuwa_FinalProject
Final Project is about e-commerce.

There are 4 different services:

* Item Service (including inventory service)
* Order Service
* Payment Service
* Account Service




User flow
1. using account (register & login)
2. Order item by order service
3. call items service by order and get the item info(availiability)

    a. create order

    b. update order

    c. cancel order
    
4. pay the items by payment service





## Item Service:

**Database: MongoDB**

**Base API URL:**  `../api/items`

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

* GET `/api/items/{id}/availability ` – Check available stock for an item

* PUT `/api/items/{id}` – Update an existing item by ID

* DELETE `/api/items/{id}` – Delete an item by ID


## Order Service:

**Database: Cassandra**

The order service supports both synchronous and asynchronous communications, it produces
Kafka messages and also consumes kafka messages.

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

* You create the order first → it stores the item, price, quantity, user info.

* Status starts as PENDING_PAYMENT.

* Inventory may be reserved here so the item is held for the user.

2. Payment Service

* You call it with that orderId and payment info.

* If payment successful:

    * Payment Service marks payment record SUCCESS.

    * Publishes a PaymentSucceeded event (Kafka).

    * Order Service listens for that event → updates order to PAID or COMPLETED (depending on your naming).

* If payment fails:

    * Payment Service marks payment record FAILED.

    * Publishes a PaymentFailed event.

    * Order Service sets order status to PAYMENT_FAILED (or leaves it PENDING_PAYMENT if you want retries).




## Account Service:

**Database: MySQL(transaction integrity)**
* Create Account
* Update Account
* Account Lookup
* ...

Users should be able to create/update their account with following necessary information:

* User Email
* User name
* Password
* Shipping Address
* Billing Address
* Payment Method
