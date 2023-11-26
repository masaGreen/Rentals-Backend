package com.masagreen.RentalUnitsManagement.dtos.unit;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import com.masagreen.RentalUnitsManagement.models.entities.Unit;

@Builder
@Data
public class UnitDataResponseDto {
    private List<Unit> units;
}
