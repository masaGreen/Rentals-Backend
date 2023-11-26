package com.masagreen.RentalUnitsManagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @Info(
                title = "OpenApi Docs for Rental-Units-Management-System",
                description = "OpenApi Docs for Rental-Units-Management-System",
                termsOfService="link to terms of service",
                contact = @Contact(email = "davidmachariamj@gmail.com")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description="Dev Env")
        }


)
// @SecurityScheme(
//         name = "bearerAuth",
//         description = "enter the jwt token",
//         scheme = "bearer",
//         type = SecuritySchemeType.HTTP,
//         bearerFormat = "JWT",
//         in = SecuritySchemeIn.HEADER
// )
@Configuration
class SwaggerConfig {

}
