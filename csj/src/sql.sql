create database smartPortable;

show databases;

use smartPortable;

CREATE TABLE UserDetail (
    userId int NOT NULL AUTO_INCREMENT,
    firstname varchar(255) NOT NULL,
    lastname varchar(255) NOT NULL,
    username varchar(255),
    userPassword varchar(255),
    userRole varchar(255),
    phone varchar(255),
    address varchar(255),
    city varchar(255),
    state varchar(255),
    country varchar(255),
    zipcode int,
    PRIMARY KEY (userId)
);

CREATE TABLE CustomerOrder (
    customerOrderId int NOT NULL AUTO_INCREMENT,
    orderId int,
    productId varchar(255) NOT NULL,
    productName varchar(255) NOT NULL,
    username varchar(255),
    creditcard varchar(255),
    orderPrice varchar(255),
    quantity varchar(255),
    retailer varchar(255),
    manufacturer varchar(255),
    currentOrderTime varchar(255),
    deliveryTime varchar(255),
    orderStatus varchar(255),
    PRIMARY KEY (customerOrderId)
);

ALTER TABLE CustomerOrder AUTO_INCREMENT=100;

ALTER TABLE UserDetail AUTO_INCREMENT=100;

CREATE TABLE Product (
    productId varchar(255) NOT NULL,
    productName varchar(255) NOT NULL,
    manufacturer varchar(255),
    productQuantity int,
    productPrice float,
    imageSrc varchar(255),
    productCondition varchar(255),
    retailer varchar(255),
    retailerDiscount float,
    manufactureRebate float,
    productCategory varchar(255),
    productOnSale varchar(225),
    PRIMARY KEY (productId)
);

show tables;

SELECT * FROM UserDetail;
SELECT * FROM CustomerOrder;
SELECT * FROM Product;

SELECT productId, productName, productPrice, productQuantity FROM Product ORDER BY productQuantity;
SELECT productId, productName, manufactureRebate FROM Product WHERE manufactureRebate > 0 ORDER BY manufactureRebate DESC;
SELECT productName FROM Product WHERE productOnSale LIKE 'Yes';

SELECT STR_TO_DATE(currentOrderTime, '%Y/%m/%d') as Dates, SUM(orderPrice) as TotalSales FROM CustomerOrder GROUP BY STR_TO_DATE(currentOrderTime, '%Y/%m/%d');

SELECT co.productId, co.productName as ProductName, p.productPrice as ProductPrice, SUM(co.quantity) as NumberOfItemsSold, (p.productPrice * SUM(co.quantity)) as TotalSales FROM CustomerOrder as co JOIN Product p ON co.productId = p.productId GROUP BY co.productId;

















SELECT * FROM CustomerOrder where username="anshika" order by orderId desc;
SELECT MAX(orderId) as orderId FROM CustomerOrder where username="anshika";



ALTER TABLE Product AUTO_INCREMENT=100;

DELETE FROM Product;
SELECT * FROM Product WHERE productCategory = 'phone';

INSERT INTO UserDetail (firstname, lastname, username, userPassword, userRole, phone, address, city, state, country, zipcode) 
VALUES ('Anshika', 'Trivedi', 'anshika', 'anshika', 'customer', '1234567891', 'King Drive', 'Chicago', 'IL', 'USA', 60616);

DROP TABLE Product;