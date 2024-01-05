package com.masagreen.RentalUnitsManagement.dtos.unit;


public record UnitReqDto(
        String plotName,
        String unitNumber,
        String tag,
        int rent) {

}


