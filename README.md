# Rentals-Backend
![Java](https://img.shields.io/badge/Java-FEB95F?style=for-the-badge&logo=java&logoColor=white)
![Spring-Boot](https://img.shields.io/badge/SpringBoot-81FF5E?style=for-the-badge&logo=spring&logoColor=white)
![MySql](https://img.shields.io/badge/MySQL-1E7AB8?style=for-the-badge&logo=mysql&logoColor=white)
![Swagger3](https://img.shields.io/badge/Swagger3-84E713?style=for-the-badge&logo=swagger&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-ef233c?style=for-the-badge&logo=jwt.io&logoColor=white)

# url hosting the apidocs

- swagger-openapi docs:
 
         

            https://rentals-management-backend.onrender.com/swagger-ui.html


https://github.com/masaGreen/Rental-Units-Management-System/assets/111172753/8a7fb3b4-3f10-42d2-8be9-34b9dd3907a8

# A rental-unit management Api made in Springboot.
Rental-Units-Management-System is a rest-api that can serve a client that provides user interface in managing rental uits.
It is made using java spring boot Framework and swagger ui is used for documentation.It is secured with spring security and json web token -jwt. Important changes are communicated with email.

 
# Overview

This Api provides necessary endpoints that a client needs to manage rental units, tenants and utilities and their payments.

# Requirements

- Java 17 or later
- MySql server running on port 3306

# Set-up

- Clone the repository:
 
         

            git clone https://github.com/yourusername/Rental-Units-Management-System.git

            
- create database db_rentals
- **NB** make sure no application is running on port 8080 otherwise change port before running the application
            

            cd Rental-Units-Management-System
            mvnw clean package

            
- The application will start at http://localhost:8080
# Local Swagger Api documentation 
- For detailed documentation on endpoints and payloads visit "http://localhost:8080/swagger-ui.html" after your app starts.
- Hit the default-authorized /v1/auth/login with the provided default login credentials to get your token inorder to test the rest of the endpoints.
  
# Authentication endpoint
```sql
 v1/auth/login
  ```
# Main Endpoints (the rest are indicated on the main documentation)
```sql
/v1/units/
/v1/tenants/
/v1/utilities/
 ```
# Features
Rental-Units-Management-System provides the following features:

* download pdfs for  tenantslist, tenants-with-arrears-list, utilspayments-list and all-units.
* Create, edit and delete units, tenants and utilities.
* Check available units
* update unit availability
* update tenant payment status


# Usage
- **NB** All users are signed-up as unapproved so to facilitate approval a default admin is automatically enabled on startup,
with email:root@gmail.com password:rootpassword. This can be changed once your custom admin(S) is enabled.

- jwt token is set as Authorization Header

- Once the server is running, you can access the API at `http://localhost:8080/`. You can use any HTTP client to interact with the API, such as curl . Alternatively, you can use swagger-ui to explore the endpoints.


# License
 Licensed under the MIT License.
