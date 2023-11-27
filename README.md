# Rentals-Backend
![Java](https://img.shields.io/badge/Java-FEB95F?style=for-the-badge&logo=java&logoColor=white)
![Spring-Boot](https://img.shields.io/badge/SpringBoot-81FF5E?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/Postgres-1E7AB8?style=for-the-badge&logo=postgres&logoColor=white)
![Swagger3](https://img.shields.io/badge/Swagger3-84E713?style=for-the-badge&logo=swagger&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-ef233c?style=for-the-badge&logo=jwt.io&logoColor=white)



- swagger-openapi docs:
 
         

            https://rental-backend-0m96.onrender.com/swagger-ui.html
 - A react ui consuming this api is hosted at:

        

          https://myrentalmanager.onrender.com

# Techstack used

- Springboot , SpringSecurity and springData
- Logstash-logback-encoder to output json formatted Logging
- Docker for deployment
- jwt role based authentication and authorization
- Postgres for database
# A rental-unit management Api made in Springboot.
Rental-Units-Management-System is a rest-api that can serve a client that provides user interface in managing rental uits.
It is made using java spring boot Framework and swagger ui is used for documentation.It is secured with spring security and json web token -jwt. Important changes are communicated with email.

 
# Overview

This Api provides necessary endpoints that a client needs to manage rental units, tenants and utilities and their payments.

# Requirements

- Java 17 or later
- sql server running 

# Set-up

- Clone the repository:
 
         

            git clone https://github.com/yourusername/Rental-Backend.git

            
- create database 
- **NB** make sure no application is running on port 8080 otherwise change port before running the application
            

            cd Rental-Backend
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
* Role based authorization to perform different actions
* download pdfs for  tenantslist, tenants-with-arrears-list, utilspayments-list and all-units.
* Create, edit and delete units, tenants and utilities.
* Check available units
* update unit availability
* update tenant payment status


- jwt token is set as Authorization Header

- Once the server is running, you can access the API at `http://localhost:8080/`. You can use any HTTP client to interact with the API, such as curl . Alternatively, you can use swagger-ui to explore the endpoints.


# License
 Licensed under the MIT License.
