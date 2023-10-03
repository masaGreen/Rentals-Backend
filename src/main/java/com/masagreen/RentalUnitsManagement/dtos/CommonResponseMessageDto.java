package com.masagreen.RentalUnitsManagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommonResponseMessageDto {
    private String message;
}
