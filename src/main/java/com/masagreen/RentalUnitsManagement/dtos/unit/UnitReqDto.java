package com.masagreen.RentalUnitsManagement.dtos.unit;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UnitReqDto {

    private String plotName;
    private String unitNumber;
    private  String tag;
    private int rent;
}

