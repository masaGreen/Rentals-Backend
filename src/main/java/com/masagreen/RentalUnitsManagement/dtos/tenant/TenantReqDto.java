package com.masagreen.RentalUnitsManagement.dtos.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantReqDto {

    private String firstName;
    private String lastName;
    private String phone;
    private String unitNumber;


}
