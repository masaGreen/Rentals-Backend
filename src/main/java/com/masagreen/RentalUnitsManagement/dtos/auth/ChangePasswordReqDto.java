package com.masagreen.RentalUnitsManagement.dtos.auth;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordReqDto {
    @Size(min = 4, message = "must be longer than four characters")
    private String oldPassword;
    @Size(min = 4, message = "must be longer than four characters")
    private String newPassword;

}
