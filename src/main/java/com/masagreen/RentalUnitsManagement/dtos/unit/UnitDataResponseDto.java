package com.masagreen.RentalUnitsManagement.dtos.unit;

import com.masagreen.RentalUnitsManagement.models.Unit;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UnitDataResponseDto {
    private List<Unit> units;
}
