## Introduction
Thank you for taking an interest in this Checkout microservice project. This small application provides a Checkout interface features to scan (add) items to checkout shopping cart and to calculate total taking in account any applicable promotions. The DiscountProcessor interface allows staff/admin users to add/remove promotions.

## Basic overview of the the application:
There are 2 main components in this application, the Checkout and DiscountProcessor. The Checkout allows items to be added (scan()) to the List (Representation of shopping cart). It also provide method to obtain total of the items in cart. To calculate the total, checkout delegates the handlingof promotion/discounts to DiscountProcessor. In practise, admin users will be able to add/remove Promotions encapsulated in the DiscountProcessor. When the DiscountProcessor is called to apply promotions to cart items, it will iterate through the promotions and apply appropriate discounts to eligible items. Note that as a requirement, discounts to be applied to the cart total are to be processed last. The DiscountProcessor changes the state of the cart by updating each item's promotional price, so that when necessary, the full list of updated itemised discounts can be passed to the front-end. 
Item entity acts as a wrapper class to the Product entity, providing extra functions and features such as setting promotional price while keeping Product class lean.

## Running Tests
There are uni tests and one integration test in the test package, to run the the tests you may simply call:

``mvn test`` on the root directory of this project.

## Potential extension of Checkout service
The main reason why Spring Boot was my framework of choice is because of the potential to build a REST API facade for this Checkout component relatively easily, with help of Spring Controller, Service and Enity classes. Making it a standalone microservice.
