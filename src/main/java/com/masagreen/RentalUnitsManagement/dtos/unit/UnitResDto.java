package com.masagreen.RentalUnitsManagement.dtos.unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UnitResDto {

    private String plotName;
    private String unitNumber;
    private  String tag;
    private boolean status;
    private int rent;
}