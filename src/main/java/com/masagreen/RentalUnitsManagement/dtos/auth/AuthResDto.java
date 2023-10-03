package com.masagreen.RentalUnitsManagement.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResDto {
    private int id;
    private String email;
    private boolean status;
    private String message;
    private String token;
    private String role;
}
