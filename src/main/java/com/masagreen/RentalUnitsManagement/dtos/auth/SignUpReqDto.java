package com.masagreen.RentalUnitsManagement.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SignUpReqDto {
    @Email(message="invalid email")
    @NotBlank
    private String email;

    // @Size(min= 4,max= 8, message="must be longer than 4 characters")
    private String password;
    @NotBlank(message="role must not be empty")
    private String role;
}
