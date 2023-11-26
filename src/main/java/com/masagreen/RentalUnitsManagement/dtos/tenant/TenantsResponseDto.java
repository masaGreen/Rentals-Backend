package com.masagreen.RentalUnitsManagement.dtos.tenant;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantsResponseDto {
    private List<TenantDTO> tenants;
}
