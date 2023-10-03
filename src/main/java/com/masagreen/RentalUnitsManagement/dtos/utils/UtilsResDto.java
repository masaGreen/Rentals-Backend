package com.masagreen.RentalUnitsManagement.dtos.utils;

import com.masagreen.RentalUnitsManagement.models.UtilitiesPayments;
import lombok.Builder;
import lombok.Data;


import java.util.List;

@Builder
@Data
public class UtilsResDto {

    private List<UtilitiesPayments> utilsPayments;
}
