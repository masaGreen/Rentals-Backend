package com.masagreen.RentalUnitsManagement.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponseDto {
    private List<SignUpResponseDto> users;
}
