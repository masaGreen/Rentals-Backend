package com.masagreen.RentalUnitsManagement.dtos.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AuthReqBodyDto {
    private String email;
    private String password;
}
