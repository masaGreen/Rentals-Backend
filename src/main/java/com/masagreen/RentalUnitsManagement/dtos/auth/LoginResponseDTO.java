package com.masagreen.RentalUnitsManagement.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class LoginResponseDTO {
    private String email;
    private boolean status;
    private String token;
    private String role;
    private String id;
}



