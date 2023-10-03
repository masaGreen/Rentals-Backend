package com.masagreen.RentalUnitsManagement.dtos.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtilsReqDto {

    private String waterBill;
    private String garbage;
    private String amountPaid;
       private String unitNumber;
}

