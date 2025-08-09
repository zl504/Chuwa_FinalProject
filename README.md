# Chuwa_FinalProject
Final Project is about e-commerce.

There are 4 different services:

* Item Service (including inventory service)
* Order Service
* Payment Service
* Account Service


## Item Service:

The item service provides item related information including unit price, item name, item picture
urls, UPC (universal product code), item id.

Item Service stores everything about an item, we usually call such data as metadata, Use **MongoDB** to realize it as it’s difficult to finalize a schema for such data.

Item service is also responsible for inventory lookup and update, by returning remaining
available units of a product.

## Order Service:

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
integrates with order service, provides REST APIs, and publish payment transaction
results to consumers.
* Submit Payment
* Update Payment
* Reverse Payment: Refund
* Payment Lookup: lookup a payment, return its status
Idempotency should be guaranteed throughout the payment flow, as we don't want to
double-charge customers or double-refund them.

**MySQL** for (transaction integrity)

## Account Service:

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

Using **MySQL**