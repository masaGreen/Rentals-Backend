package com.masagreen.RentalUnitsManagement.dtos.tenant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatusUpdateReqDto {
    private String phone;
    private String payStatus;
}
