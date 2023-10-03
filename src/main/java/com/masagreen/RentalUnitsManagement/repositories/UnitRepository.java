package com.masagreen.RentalUnitsManagement.repositories;

import com.masagreen.RentalUnitsManagement.models.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByUnitNumber(String unitNumber);
}
