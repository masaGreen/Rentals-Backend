package com.masagreen.RentalUnitsManagement.dtos.auth;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record ChangePasswordReqDto (
    @Size(min = 4, message = "must be longer than four characters")
    String oldPassword,
    @Size(min = 4, message = "must be longer than four characters")
    String newPassword

){}
