package com.masagreen.RentalUnitsManagement.dtos.utils;

import lombok.Builder;
import lombok.Data;


import java.util.List;

import com.masagreen.RentalUnitsManagement.models.entities.UtilitiesPayments;

@Builder
@Data
public class UtilsResDto {

    private List<UtilitiesPayments> utilsPayments;
}
