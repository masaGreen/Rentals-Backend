package com.masagreen.RentalUnitsManagement.repositories;

import com.masagreen.RentalUnitsManagement.models.entities.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, String> {
    Optional<Unit> findByUnitNumber(String unitNumber);

    Boolean existsByUnitNumber(String unitNumber);

    @Query(
            "SELECT u FROM Unit u WHERE u.status = true"
    )
    List<Unit> findAllUnitsAvailable();
}
